package com.phonepe.commons.columbus.elastic9.internal.storage.handlers.entity.bulk;

import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.IndexOperation;
import com.phonepe.commons.columbus.dao.id.Id;
import com.phonepe.commons.columbus.dao.misc.OperationFailure;
import com.phonepe.commons.columbus.elastic9.common.Constants.Operations;
import com.phonepe.commons.columbus.elastic9.internal.search.utils.MiscTransforms;
import com.phonepe.commons.columbus.elastic9.internal.storage.utils.ElasticClientUtils;
import com.phonepe.commons.columbus.elastic9.internal.storage.utils.EntityTransforms;
import com.phonepe.commons.columbus.elastic9.internal.storage.utils.EnumTransforms;
import com.phonepe.commons.columbus.elastic9.storage.ElasticClient;
import com.phonepe.commons.columbus.elastic9.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.commons.columbus.elastic9.storage.toolkit.ElasticToolkit;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.storage.commands.entity.bulk.BulkInsertCommand;
import com.phonepe.commons.columbus.storage.models.entity.Entity;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

/**
 * Insert a batch of values into an ElasticSearch index.
 *
 * @param <E> the type of the Elastic client.
 */
@Slf4j
@RegistryKey(name = BulkInsertCommand.COMMAND_NAME, type = BulkInsertCommand.class)
public class BulkInsertHandler<T, E extends ElasticClient<E>>
        implements ElasticStorageCommandHandler<BulkInsertCommand.Result, BulkInsertCommand<T>, E> {

    @Override
    @SneakyThrows
    public BulkInsertCommand.Result handle(final BulkInsertCommand<T> command, final E storage) {
        final var context = new Context();
        context.toolkit = storage.toolkit();

        final var request = createRequest(command, context);

        final BulkResponse response;
        try {
            response = bulkInsert(storage, request);
        } catch (final IOException e) {
            log.error("Failed to insert into index '{}' with {} entities", context.indexName, context.docCount, e);
            throw e;
        }

        return parseResult(response, context);
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.BULK_INDEX_DOC)
    private BulkResponse bulkInsert(final E storage, final BulkRequest request) throws IOException {
        return storage.client().bulk(request);
    }

    private BulkRequest createRequest(final BulkInsertCommand<T> command, final Context context) {
        context.docCount = command.getEntities().size();
        context.idMap = new HashMap<>(context.docCount);
        context.indexName = ElasticClientUtils.resolveIndexName(context.toolkit, command.getIndex());

        final var request = new BulkRequest.Builder()
                .index(context.indexName);
        command.getEntities().stream()
                .map(entity -> createIndexRequest(entity, context))
                .forEachOrdered(request::operations);
        applyIfNotNull(command.getExecutionPolicy(), EnumTransforms::toRefreshPolicy, request::refresh);
        return request.build();
    }

    private BulkInsertCommand.Result parseResult(final BulkResponse response, final Context context) {
        final var builder = BulkInsertCommand.Result.builder();
        final var idMap = context.idMap;

        response.items().stream()
                .filter(item -> item.error() != null)
                .forEach(item -> builder.failure(
                        idMap.get(item.id()),
                        OperationFailure.builder()
                                .message(MiscTransforms.fromElastic(item.error()))
                                .build()));

        return builder.build();
    }

    private IndexOperation<T> createIndexRequest(final Entity<T> entity, final Context context) {
        final var id = ElasticClientUtils.resolveId(context.toolkit, entity.getId());
        final var node = ElasticClientUtils.resolveNodeId(context.toolkit, entity.getNodeId());
        context.idMap.put(id, entity.getId());
        return new IndexOperation.Builder<T>()
                .id(id)
                .routing(EntityTransforms.getSingleRouting(node))
                .document(entity.getValue())
                .build();
    }

    private static final class Context {

        private ElasticToolkit toolkit;
        private String indexName;
        private int docCount;
        private Map<String, Id> idMap;
    }
}
