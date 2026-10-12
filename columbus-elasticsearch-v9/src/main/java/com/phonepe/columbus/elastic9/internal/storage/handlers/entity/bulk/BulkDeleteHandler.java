package com.phonepe.columbus.elastic9.internal.storage.handlers.entity.bulk;

import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.DeleteOperation;
import com.phonepe.columbus.dao.id.Id;
import com.phonepe.columbus.dao.misc.OperationFailure;
import com.phonepe.columbus.elastic9.common.Constants.Operations;
import com.phonepe.columbus.elastic9.internal.search.utils.MiscTransforms;
import com.phonepe.columbus.elastic9.internal.storage.utils.ElasticClientUtils;
import com.phonepe.columbus.elastic9.internal.storage.utils.EntityTransforms;
import com.phonepe.columbus.elastic9.internal.storage.utils.EnumTransforms;
import com.phonepe.columbus.elastic9.storage.ElasticClient;
import com.phonepe.columbus.elastic9.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.columbus.elastic9.storage.toolkit.ElasticToolkit;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.commands.entity.bulk.BulkDeleteCommand;
import com.phonepe.columbus.storage.models.entity.EntityId;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

/**
 * Delete a batch of values from an ElasticSearch index.
 *
 * @param <E> the type of the Elastic client.
 */
@Slf4j
@RegistryKey(name = BulkDeleteCommand.COMMAND_NAME, type = BulkDeleteCommand.class)
public class BulkDeleteHandler<E extends ElasticClient<E>>
        implements ElasticStorageCommandHandler<BulkDeleteCommand.Result, BulkDeleteCommand, E> {

    @Override
    @SneakyThrows
    public BulkDeleteCommand.Result handle(final BulkDeleteCommand command, final E storage) {
        final var context = new Context();
        context.toolkit = storage.toolkit();

        final var request = createRequest(command, context);

        final BulkResponse response;
        try {
            response = bulkDelete(storage, request);
        } catch (final IOException e) {
            log.error("Failed to delete from index '{}' with {} ids", context.indexName, context.docCount, e);
            throw e;
        }

        return parseResult(response, context);
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.BULK_DELETE_DOC)
    private BulkResponse bulkDelete(final E storage, final BulkRequest request) throws IOException {
        return storage.client().bulk(request);
    }

    private BulkRequest createRequest(final BulkDeleteCommand command, final Context context) {
        context.docCount = command.getIds().size();
        context.idMap = new HashMap<>(context.docCount);
        context.indexName = ElasticClientUtils.resolveIndexName(context.toolkit, command.getIndex());

        final var request = new BulkRequest.Builder().index(context.indexName);
        command.getIds().stream()
                .map(id -> createDeleteRequest(id, context))
                .forEachOrdered(request::operations);
        applyIfNotNull(command.getExecutionPolicy(), EnumTransforms::toRefreshPolicy, request::refresh);
        return request.build();
    }

    private BulkDeleteCommand.Result parseResult(final BulkResponse response, final Context context) {
        final var builder = BulkDeleteCommand.Result.builder();
        final var idMap = context.idMap;

        response.items().forEach(item -> {
            final var id = idMap.get(item.id());
            if (item.error() != null) {
                builder.failure(id, OperationFailure.builder()
                        .message(MiscTransforms.fromElastic(item.error()))
                        .build());
            } else if (Result.NotFound.jsonValue().equals(item.result())) {
                builder.notFound(id);
            }
        });
        return builder.build();
    }

    private DeleteOperation createDeleteRequest(final EntityId entity, final Context context) {
        final var id = ElasticClientUtils.resolveId(context.toolkit, entity.getId());
        final var node = ElasticClientUtils.resolveNodeId(context.toolkit, entity.getNodeId());
        context.idMap.put(id, entity.getId());
        return new DeleteOperation.Builder()
                .id(id)
                .routing(EntityTransforms.getSingleRouting(node))
                .build();
    }

    private static final class Context {

        private ElasticToolkit toolkit;
        private String indexName;
        private int docCount;
        private Map<String, Id> idMap;
    }
}
