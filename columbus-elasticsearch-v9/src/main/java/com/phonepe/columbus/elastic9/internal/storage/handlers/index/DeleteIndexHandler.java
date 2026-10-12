package com.phonepe.columbus.elastic9.internal.storage.handlers.index;

import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import com.phonepe.columbus.elastic9.common.Constants.Operations;
import com.phonepe.columbus.elastic9.storage.ElasticClient;
import com.phonepe.columbus.elastic9.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.commands.index.DeleteIndexCommand;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Delete an existing index.
 * <p>
 * The result indicates if the server has acknowledged the request. The actual deletion might occur later, resulting in
 * some (short-lived) inconsistency.
 *
 * @param <E> the type of the Elastic client.
 */
@Slf4j
@RegistryKey(name = DeleteIndexCommand.COMMAND_NAME, type = DeleteIndexCommand.class)
public class DeleteIndexHandler<E extends ElasticClient<E>>
        implements ElasticStorageCommandHandler<Boolean, DeleteIndexCommand, E> {

    @Override
    @SneakyThrows
    public Boolean handle(final DeleteIndexCommand command, final E storage) {
        final var indexName = storage.toolkit().indexResolver().resolve(command.getIndex()).getName();
        final var request = new DeleteIndexRequest.Builder().index(indexName).build();

        try {
            final var ack = deleteIndex(storage, request).acknowledged();
            if (!ack) {
                log.warn("Deletion not acknowledged for index: '{}'", indexName);
            }
            return ack;
        } catch (final IOException e) {
            log.error("Failed to delete index '{}' with error", indexName, e);
            throw e;
        }
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.DELETE_INDEX)
    private DeleteIndexResponse deleteIndex(final E storage, final DeleteIndexRequest request) throws IOException {
        return storage.client().indices().delete(request);
    }
}
