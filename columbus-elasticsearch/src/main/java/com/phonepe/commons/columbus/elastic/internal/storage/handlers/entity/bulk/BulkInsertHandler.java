package com.phonepe.commons.columbus.elastic.internal.storage.handlers.entity.bulk;

import com.phonepe.commons.columbus.dao.id.Id;
import com.phonepe.commons.columbus.dao.misc.OperationFailure;
import com.phonepe.commons.columbus.elastic.common.Constants.Operations;
import com.phonepe.commons.columbus.elastic.internal.storage.utils.ElasticClientUtils;
import com.phonepe.commons.columbus.elastic.internal.storage.utils.EntityTransforms;
import com.phonepe.commons.columbus.elastic.internal.storage.utils.EnumTransforms;
import com.phonepe.commons.columbus.elastic.storage.ElasticClient;
import com.phonepe.commons.columbus.elastic.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.commons.columbus.elastic.storage.toolkit.ElasticToolkit;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.storage.commands.entity.bulk.BulkInsertCommand;
import com.phonepe.commons.columbus.storage.models.entity.Entity;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.Arrays;
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
        } catch (IOException e) {
            log.error("Failed to insert into index '{}' with {} entities", context.indexName, context.docCount, e);
            throw e;
        }

        return parseResult(response, context);
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.BULK_INDEX_DOC)
    private BulkResponse bulkInsert(final E storage, final BulkRequest request) throws IOException {
        return storage.client().bulk(request, RequestOptions.DEFAULT);
    }

    private BulkRequest createRequest(final BulkInsertCommand<T> command, final Context context) {
        context.docCount = command.getEntities().size();
        context.idMap = new HashMap<>(context.docCount);
        context.indexName = ElasticClientUtils.resolveIndexName(context.toolkit, command.getIndex());

        final var request = new BulkRequest(context.indexName);
        command.getEntities().stream()
                .map(entity -> createIndexRequest(entity, context))
                .forEachOrdered(request::add);
        applyIfNotNull(command.getExecutionPolicy(), EnumTransforms::toRefreshPolicy, request::setRefreshPolicy);
        return request;
    }

    private BulkInsertCommand.Result parseResult(final BulkResponse response, final Context context) {
        final var builder = BulkInsertCommand.Result.builder();
        final var idMap = context.idMap;

        Arrays.stream(response.getItems())
                .filter(BulkItemResponse::isFailed)
                .map(BulkItemResponse::getFailure)
                .forEach(failure -> builder.failure(
                        idMap.get(failure.getId()),
                        OperationFailure.builder()
                                .message(failure.getMessage())
                                .exception(failure.getCause())
                                .build()));

        return builder.build();
    }

    private IndexRequest createIndexRequest(final Entity<T> entity, final Context context) {
        final var id = ElasticClientUtils.resolveId(context.toolkit, entity.getId());
        final var node = ElasticClientUtils.resolveNodeId(context.toolkit, entity.getNodeId());
        context.idMap.put(id, entity.getId());
        final var serDe = context.toolkit.serDe();
        return new IndexRequest()
                .id(id)
                .routing(EntityTransforms.getSingleRouting(node))
                .source(serDe.toBytes(entity.getValue()), XContentType.fromMediaType(serDe.mediaType()));
    }

    private static final class Context {

        private ElasticToolkit toolkit;
        private String indexName;
        private int docCount;
        private Map<String, Id> idMap;
    }
}
