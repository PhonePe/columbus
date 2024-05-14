package com.phonepe.commons.columbus.elastic.internal.storage.handlers.entity.bulk;

import com.phonepe.commons.columbus.dao.id.Ids;
import com.phonepe.commons.columbus.dao.index.Indices;
import com.phonepe.commons.columbus.dao.misc.EntityOperationFailure;
import com.phonepe.commons.columbus.dao.misc.IndexOperationFailure;
import com.phonepe.commons.columbus.elastic.common.Constants.Operations;
import com.phonepe.commons.columbus.elastic.internal.search.utils.MiscTransforms;
import com.phonepe.commons.columbus.elastic.internal.storage.utils.ElasticClientUtils;
import com.phonepe.commons.columbus.elastic.storage.ElasticClient;
import com.phonepe.commons.columbus.elastic.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.storage.commands.entity.bulk.CopyCommand;
import com.phonepe.commons.columbus.storage.models.RequestExecutionPolicy;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.tasks.TaskSubmissionResponse;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.ReindexRequest;

import java.io.IOException;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

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
    private BulkByScrollResponse reindex(final E storage, final ReindexRequest request) throws IOException {
        return storage.client().reindex(request, RequestOptions.DEFAULT);
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.REINDEX_ASYNC)
    private TaskSubmissionResponse reindexAsync(final E storage, final ReindexRequest request) throws IOException {
        return storage.client().submitReindexTask(request, RequestOptions.DEFAULT);
    }

    private CopyCommand.Result runTask(final E storage, final ReindexRequest request) throws IOException {
        final BulkByScrollResponse response;
        try {
            response = reindex(storage, request);
        } catch (IOException e) {
            log.error("Failed to reindex: {} with error", request, e);
            throw e;
        }
        return CopyCommand.Result.builder()
                .total(response.getTotal())
                .created(response.getCreated())
                .updated(response.getUpdated())
                .writeFailures(response.getBulkFailures().stream()
                        .map(failure -> EntityOperationFailure.builder()
                                .id(Ids.value(failure.getId()))
                                .index(Indices.name(failure.getIndex()))
                                .message(failure.getMessage())
                                .exception(failure.getCause())
                                .build())
                        .toList())
                .searchFailures(response.getSearchFailures()
                        .stream()
                        .map(failure -> IndexOperationFailure.builder()
                                .index(Indices.name(failure.getIndex()))
                                .message(failure.toString())
                                .exception(failure.getReason())
                                .build())
                        .toList())
                .build();
    }

    private CopyCommand.Result submitTask(final E storage, final ReindexRequest request) throws IOException {
        final TaskSubmissionResponse response;
        try {
            response = reindexAsync(storage, request);
        } catch (IOException e) {
            log.error("Failed to submit reindex task: {} with error", request, e);
            throw e;
        }
        return CopyCommand.Result.builder()
                .taskId(response.getTask())
                .build();
    }

    private ReindexRequest buildRequest(final CopyCommand command, final E storage) {
        if (command.getSourceIndices().isEmpty()) {
            throw new IllegalArgumentException("Require at lease one source index");
        }

        final var toolkit = storage.toolkit();
        final var sourceIndices = command.getSourceIndices().stream()
                .map(index -> ElasticClientUtils.resolveIndexName(toolkit, index))
                .toArray(String[]::new);

        final var request = new ReindexRequest()
                .setSourceIndices(sourceIndices)
                .setDestIndex(ElasticClientUtils.resolveIndexName(toolkit, command.getDestinationIndex()))
                .setAbortOnVersionConflict(command.failOnError());

        request.getDestination().create(command.skipExisting());

        applyIfNotNull(command.getQuery(), toolkit.queryTransformer()::transform, request::setSourceQuery);
        applyIfNotNull(
                command.getProjection(),
                MiscTransforms::toElastic,
                request.getSearchRequest().source()::fetchSource);
        applyIfNotNull(command.getMaximumCount(), request::setMaxDocs);
        applyIfNotNull(command.getBatchSize(), request::setSourceBatchSize);
        applyIfNotNull(
                command.getExecutionPolicy(),
                RequestExecutionPolicy.FORCE_COMPLETION::equals,
                request::setRefresh);

        return request;
    }
}
