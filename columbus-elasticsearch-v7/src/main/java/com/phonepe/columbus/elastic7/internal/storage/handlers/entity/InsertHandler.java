package com.phonepe.columbus.elastic7.internal.storage.handlers.entity;

import com.phonepe.columbus.elastic7.common.Constants.Operations;
import com.phonepe.columbus.elastic7.internal.storage.utils.ElasticClientUtils;
import com.phonepe.columbus.elastic7.storage.ElasticClient;
import com.phonepe.columbus.elastic7.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.commands.entity.InsertCommand;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;

/**
 * Insert a value to an index in ElasticSearch.
 *
 * @param <E> the type of the Elastic client.
 */
@Slf4j
@RegistryKey(name = InsertCommand.COMMAND_NAME, type = InsertCommand.class)
public class InsertHandler<T, E extends ElasticClient<E>>
        implements ElasticStorageCommandHandler<Void, InsertCommand<T>, E> {

    @Override
    @SneakyThrows
    public Void handle(final InsertCommand<T> command, final E storage) {
        final var toolkit = storage.toolkit();
        final var indexName = ElasticClientUtils.resolveIndexName(toolkit, command.getIndex());
        final var id = ElasticClientUtils.resolveId(toolkit, command.getId());
        final var routing = ElasticClientUtils.resolveSingleRouting(toolkit, command.getNodeId());
        final var serDe = toolkit.serDe();

        final var request = new IndexRequest(indexName)
                .id(id)
                .routing(routing)
                .source(serDe.toBytes(command.getValue()), XContentType.fromMediaType(serDe.mediaType()));

        try {
            insert(storage, request);
        } catch (IOException e) {
            log.error("Failed to insert to index '{}' with id '{}'", indexName, id, e);
            throw e;
        }
        return null;
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.INDEX_DOC)
    private void insert(final E storage, final IndexRequest request) throws IOException {
        storage.client().index(request, RequestOptions.DEFAULT);
    }
}
