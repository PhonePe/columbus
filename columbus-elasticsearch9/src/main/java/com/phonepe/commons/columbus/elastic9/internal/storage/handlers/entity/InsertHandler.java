package com.phonepe.commons.columbus.elastic9.internal.storage.handlers.entity;

import co.elastic.clients.elasticsearch.core.IndexRequest;
import com.phonepe.commons.columbus.elastic9.common.Constants.Operations;
import com.phonepe.commons.columbus.elastic9.internal.storage.utils.ElasticClientUtils;
import com.phonepe.commons.columbus.elastic9.storage.ElasticClient;
import com.phonepe.commons.columbus.elastic9.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.storage.commands.entity.InsertCommand;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

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

        final var request = new IndexRequest.Builder<T>()
                .index(indexName)
                .id(id)
                .routing(routing)
                .document(command.getValue())
                .build();

        try {
            insert(storage, request);
        } catch (final IOException e) {
            log.error("Failed to insert to index '{}' with id '{}'", indexName, id, e);
            throw e;
        }
        return null;
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.INDEX_DOC)
    private void insert(final E storage, final IndexRequest<T> request) throws IOException {
        storage.client().index(request);
    }
}
