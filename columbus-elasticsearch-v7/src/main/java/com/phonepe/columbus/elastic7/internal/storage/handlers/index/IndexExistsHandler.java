package com.phonepe.columbus.elastic7.internal.storage.handlers.index;

import com.phonepe.columbus.elastic7.common.Constants.Operations;
import com.phonepe.columbus.elastic7.storage.ElasticClient;
import com.phonepe.columbus.elastic7.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.commands.index.IndexExistsCommand;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.GetIndexRequest;

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
        final var request = new GetIndexRequest(indexName);
        try {
            return doesIndexExist(storage, request);
        } catch (IOException e) {
            log.error("Failed to check if index '{}' exists with error", indexName, e);
            throw e;
        }
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.INDEX_EXISTS)
    private boolean doesIndexExist(final E storage, final GetIndexRequest request) throws IOException {
        return storage.client().indices().exists(request, RequestOptions.DEFAULT);
    }
}
