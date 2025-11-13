package com.phonepe.commons.columbus.elastic9.internal.storage.handlers.index;

import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import com.phonepe.commons.columbus.elastic9.common.Constants.Operations;
import com.phonepe.commons.columbus.elastic9.storage.ElasticClient;
import com.phonepe.commons.columbus.elastic9.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.storage.commands.index.IndexExistsCommand;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Check if an index with the given name exists.
 *
 * @param <E> the type of the Elastic client.
 */
@Slf4j
@RegistryKey(name = IndexExistsCommand.COMMAND_NAME, type = IndexExistsCommand.class)
public class IndexExistsHandler<E extends ElasticClient<E>>
        implements ElasticStorageCommandHandler<Boolean, IndexExistsCommand, E> {

    @Override
    @SneakyThrows
    public Boolean handle(final IndexExistsCommand command, final E storage) {
        final var indexName = storage.toolkit().indexResolver().resolve(command.getIndex()).getName();
        final var request = new ExistsRequest.Builder().index(indexName).build();
        try {
            return doesIndexExist(storage, request);
        } catch (final IOException e) {
            log.error("Failed to check if index '{}' exists with error", indexName, e);
            throw e;
        }
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.INDEX_EXISTS)
    private boolean doesIndexExist(final E storage, final ExistsRequest request) throws IOException {
        return storage.client().indices().exists(request).value();
    }
}
