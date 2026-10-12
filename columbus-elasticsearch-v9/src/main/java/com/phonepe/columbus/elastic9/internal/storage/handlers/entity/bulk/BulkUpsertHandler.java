package com.phonepe.columbus.elastic9.internal.storage.handlers.entity.bulk;

import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.UpdateAction;
import co.elastic.clients.elasticsearch.core.bulk.UpdateOperation;
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
import com.phonepe.columbus.storage.commands.entity.bulk.BulkUpsertCommand;
import com.phonepe.columbus.storage.models.entity.Entity;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

/**
 * Upsert a batch of values into an ElasticSearch index.
 *
 * @param <E> the type of the Elastic client.
 */
@Slf4j
@RegistryKey(name = BulkUpsertCommand.COMMAND_NAME, type = BulkUpsertCommand.class)
public class BulkUpsertHandler<T, E extends ElasticClient<E>>
        implements ElasticStorageCommandHandler<BulkUpsertCommand.Result, BulkUpsertCommand<T>, E> {

    @Override
    @SneakyThrows
    public BulkUpsertCommand.Result handle(final BulkUpsertCommand<T> command, final E storage) {
        final var context = new Context();
        context.toolkit = storage.toolkit();

        final var request = createRequest(command, context);

        final BulkResponse response;
        try {
            response = bulkUpsert(storage, request);
        } catch (final IOException e) {
            log.error("Failed to upsert into index '{}' with {} entities", context.indexName, context.docCount, e);
            throw e;
        }

        return parseResult(response, context);
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.BULK_UPSERT_DOC)
    private BulkResponse bulkUpsert(final E storage, final BulkRequest request) throws IOException {
        return storage.client().bulk(request);
    }

    private BulkRequest createRequest(final BulkUpsertCommand<T> command, final Context context) {
        context.docCount = command.getEntities().size();
        context.idMap = new HashMap<>(context.docCount);
        context.indexName = ElasticClientUtils.resolveIndexName(context.toolkit, command.getIndex());

        final var request = new BulkRequest.Builder()
                .index(context.indexName);
        command.getEntities().stream()
                .map(entity -> createUpdateRequest(entity, context))
                .forEachOrdered(request::operations);
        applyIfNotNull(command.getExecutionPolicy(), EnumTransforms::toRefreshPolicy, request::refresh);
        return request.build();
    }

    private BulkUpsertCommand.Result parseResult(final BulkResponse response, final Context context) {
        final var builder = BulkUpsertCommand.Result.builder();
        final var idMap = context.idMap;

        response.items().forEach(item -> {
            final var id = idMap.get(item.id());
            if (item.error() != null) {
                builder.failure(id, OperationFailure.builder()
                        .message(MiscTransforms.fromElastic(item.error()))
                        .build());
            } else {
                builder.success(id, EnumTransforms.toUpsertResult(item.result()));
            }
        });

        return builder.build();
    }

    private UpdateOperation<T, T> createUpdateRequest(final Entity<T> entity, final Context context) {
        final var id = ElasticClientUtils.resolveId(context.toolkit, entity.getId());
        final var node = ElasticClientUtils.resolveNodeId(context.toolkit, entity.getNodeId());
        context.idMap.put(id, entity.getId());
        return new UpdateOperation.Builder<T, T>()
                .id(id)
                .routing(EntityTransforms.getSingleRouting(node))
                .action(new UpdateAction.Builder<T, T>()
                        .doc(entity.getValue())
                        .docAsUpsert(true)
                        .build())
                .build();
    }

    private static final class Context {

        private ElasticToolkit toolkit;
        private String indexName;
        private int docCount;
        private Map<String, Id> idMap;
    }
}
