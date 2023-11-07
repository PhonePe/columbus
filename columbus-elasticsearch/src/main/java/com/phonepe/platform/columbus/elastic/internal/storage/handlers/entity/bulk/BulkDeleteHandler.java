package com.phonepe.platform.columbus.elastic.internal.storage.handlers.entity.bulk;

import com.phonepe.platform.columbus.dao.id.Id;
import com.phonepe.platform.columbus.dao.misc.OperationFailure;
import com.phonepe.platform.columbus.elastic.common.Constants.Operations;
import com.phonepe.platform.columbus.elastic.internal.storage.utils.ElasticClientUtils;
import com.phonepe.platform.columbus.elastic.internal.storage.utils.EntityTransforms;
import com.phonepe.platform.columbus.elastic.internal.storage.utils.EnumTransforms;
import com.phonepe.platform.columbus.elastic.storage.ElasticClient;
import com.phonepe.platform.columbus.elastic.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.platform.columbus.elastic.storage.toolkit.ElasticToolkit;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.storage.commands.entity.bulk.BulkDeleteCommand;
import com.phonepe.platform.columbus.storage.commands.entity.bulk.BulkDeleteCommand.Result;
import com.phonepe.platform.columbus.storage.models.entity.EntityId;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.client.RequestOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.phonepe.platform.columbus.common.Predicates.applyIfNotNull;

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
        } catch (IOException e) {
            log.error("Failed to delete from index '{}' with {} ids", context.indexName, context.docCount, e);
            throw e;
        }

        return parseResult(response, context);
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.BULK_DELETE_DOC)
    private BulkResponse bulkDelete(final E storage, final BulkRequest request) throws IOException {
        return storage.client().bulk(request, RequestOptions.DEFAULT);
    }

    private BulkRequest createRequest(final BulkDeleteCommand command, final Context context) {
        context.docCount = command.getIds().size();
        context.idMap = new HashMap<>(context.docCount);
        context.indexName = ElasticClientUtils.resolveIndexName(context.toolkit, command.getIndex());

        final var request = new BulkRequest(context.indexName);
        command.getIds().stream()
                .map(id -> createDeleteRequest(id, context))
                .forEachOrdered(request::add);
        applyIfNotNull(command.getExecutionPolicy(), EnumTransforms::toRefreshPolicy, request::setRefreshPolicy);
        return request;
    }

    private Result parseResult(final BulkResponse response, final Context context) {
        final var builder = BulkDeleteCommand.Result.builder();
        final var idMap = context.idMap;

        response.forEach(item -> {
            final var id = idMap.get(item.getId());
            if (item.isFailed()) {
                final var failure = item.getFailure();
                builder.failure(id, OperationFailure.builder()
                        .message(failure.getMessage())
                        .exception(failure.getCause())
                        .build());
            } else if (item.getResponse().getResult() == DocWriteResponse.Result.NOT_FOUND) {
                builder.notFound(id);
            }
        });
        return builder.build();
    }

    private DeleteRequest createDeleteRequest(final EntityId entity, final Context context) {
        final var id = ElasticClientUtils.resolveId(context.toolkit, entity.getId());
        final var node = ElasticClientUtils.resolveNodeId(context.toolkit, entity.getNodeId());
        context.idMap.put(id, entity.getId());
        return new DeleteRequest()
                .id(id)
                .routing(EntityTransforms.getSingleRouting(node));
    }

    private static final class Context {

        private ElasticToolkit toolkit;
        private String indexName;
        private int docCount;
        private Map<String, Id> idMap;
    }
}
