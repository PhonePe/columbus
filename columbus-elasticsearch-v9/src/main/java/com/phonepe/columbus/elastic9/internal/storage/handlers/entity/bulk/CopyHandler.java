package com.phonepe.columbus.elastic9.internal.storage.handlers.entity.bulk;

import co.elastic.clients.elasticsearch._types.Conflicts;
import co.elastic.clients.elasticsearch._types.OpType;
import co.elastic.clients.elasticsearch.core.ReindexRequest;
import co.elastic.clients.elasticsearch.core.ReindexResponse;
import co.elastic.clients.elasticsearch.core.reindex.Destination;
import co.elastic.clients.elasticsearch.core.reindex.Source;
import com.phonepe.columbus.dao.id.Ids;
import com.phonepe.columbus.dao.index.Indices;
import com.phonepe.columbus.dao.misc.EntityOperationFailure;
import com.phonepe.columbus.elastic9.common.Constants.Operations;
import com.phonepe.columbus.elastic9.internal.search.utils.MiscTransforms;
import com.phonepe.columbus.elastic9.internal.storage.utils.ElasticClientUtils;
import com.phonepe.columbus.elastic9.storage.ElasticClient;
import com.phonepe.columbus.elastic9.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.commands.entity.bulk.CopyCommand;
import com.phonepe.columbus.storage.models.RequestExecutionPolicy;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@Slf4j
@RegistryKey(name = CopyCommand.COMMAND_NAME, type = CopyCommand.class)
public class CopyHandler<E extends ElasticClient<E>>
        implements ElasticStorageCommandHandler<CopyCommand.Result, CopyCommand, E> {

    @Override
    @SneakyThrows
    public CopyCommand.Result handle(final CopyCommand command, final E storage) {
        final var request = buildRequest(command, storage);

        if (RequestExecutionPolicy.SUBMIT.equals(command.getExecutionPolicy())) {
            return submitTask(storage, request);
        }
        return runTask(storage, request);
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.REINDEX_DOCS)
    private ReindexResponse reindex(final E storage, final ReindexRequest request) throws IOException {
        return storage.client().reindex(request);
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.REINDEX_ASYNC)
    private ReindexResponse reindexAsync(final E storage, final ReindexRequest request) throws IOException {
        return storage.client().reindex(request);
    }

    private CopyCommand.Result runTask(final E storage, final ReindexRequest request) throws IOException {
        final ReindexResponse response;
        try {
            response = reindex(storage, request);
        } catch (final IOException e) {
            log.error("Failed to reindex: {} with error", request, e);
            throw e;
        }

        return CopyCommand.Result.builder()
                .total(response.total())
                .created(response.created())
                .updated(response.updated())
                .writeFailures(response.failures().stream()
                        .map(failure -> EntityOperationFailure.builder()
                                .id(Ids.value(failure.id()))
                                .index(Indices.name(failure.index()))
                                .message(MiscTransforms.fromElastic(failure.cause()))
                                .build())
                        .toList())
                .build();
    }

    private CopyCommand.Result submitTask(final E storage, final ReindexRequest request) throws IOException {
        final ReindexResponse response;
        try {
            response = reindexAsync(storage, request);
        } catch (final IOException e) {
            log.error("Failed to submit reindex task: {} with error", request, e);
            throw e;
        }
        return CopyCommand.Result.builder()
                .taskId(response.task())
                .build();
    }

    private ReindexRequest buildRequest(final CopyCommand command, final E storage) {
        if (command.getSourceIndices().isEmpty()) {
            throw new IllegalArgumentException("Require at lease one source index");
        }

        final var toolkit = storage.toolkit();
        final var sourceIndices = command.getSourceIndices().stream()
                .map(index -> ElasticClientUtils.resolveIndexName(toolkit, index))
                .toList();

        final var source = new Source.Builder().index(sourceIndices);
        applyIfNotNull(command.getQuery(), toolkit.queryTransformer()::transform, source::query);
        applyIfNotNull(command.getProjection(), projection -> source.sourceFields(projection.getIncludeFields()));

        final var request = new ReindexRequest.Builder()
                .source(source.build())
                .dest(new Destination.Builder()
                        .index(ElasticClientUtils.resolveIndexName(toolkit, command.getDestinationIndex()))
                        .opType(command.skipExisting() ? OpType.Create : OpType.Index)
                        .build())
                .conflicts(command.failOnError() ? Conflicts.Abort : Conflicts.Proceed)
                .waitForCompletion(!RequestExecutionPolicy.SUBMIT.equals(command.getExecutionPolicy()));

        applyIfNotNull(command.getMaximumCount(), Integer::longValue, request::maxDocs);
        applyIfNotNull(command.getBatchSize(), Integer::longValue, request::size);
        applyIfNotNull(
                command.getExecutionPolicy(),
                RequestExecutionPolicy.FORCE_COMPLETION::equals,
                request::refresh);

        return request.build();
    }
}
