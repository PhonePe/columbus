package com.phonepe.columbus.elastic9.internal.storage.handlers.entity;

import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch.core.DeleteRequest;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import com.phonepe.columbus.elastic9.common.Constants.Operations;
import com.phonepe.columbus.elastic9.internal.storage.utils.ElasticClientUtils;
import com.phonepe.columbus.elastic9.storage.ElasticClient;
import com.phonepe.columbus.elastic9.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.commands.entity.DeleteCommand;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Delete a value from an ElasticSearch index by id.
 *
 * @param <E> the type of the Elastic client.
 */
@Slf4j
@RegistryKey(name = DeleteCommand.COMMAND_NAME, type = DeleteCommand.class)
public class DeleteHandler<E extends ElasticClient<E>>
        implements ElasticStorageCommandHandler<Void, DeleteCommand, E> {

    @Override
    @SneakyThrows
    public Void handle(final DeleteCommand command, final E storage) {
        final var toolkit = storage.toolkit();
        final var indexName = ElasticClientUtils.resolveIndexName(toolkit, command.getIndex());
        final var id = ElasticClientUtils.resolveId(toolkit, command.getId());
        final var routing = ElasticClientUtils.resolveSingleRouting(toolkit, command.getNodeId());

        final var request = new DeleteRequest.Builder()
                .index(indexName)
                .id(id)
                .routing(routing)
                .build();

        final DeleteResponse response;
        try {
            response = delete(storage, request);
        } catch (final IOException e) {
            log.error("Failed to delete from index '{}' with id '{}'", indexName, id, e);
            throw e;
        }

        if (response.result() == Result.NotFound) {
            throw new IllegalArgumentException("No entity found for id: '%s'".formatted(id));
        }
        return null;
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.DELETE_DOC)
    private DeleteResponse delete(final E storage, final DeleteRequest request) throws IOException {
        return storage.client().delete(request);
    }
}
