package com.phonepe.columbus.elastic7.internal.storage.handlers.entity;

import com.phonepe.columbus.elastic7.common.Constants.Operations;
import com.phonepe.columbus.elastic7.internal.storage.utils.ElasticClientUtils;
import com.phonepe.columbus.elastic7.storage.ElasticClient;
import com.phonepe.columbus.elastic7.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.commands.entity.DeleteCommand;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.RequestOptions;

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

        final var request = new DeleteRequest(indexName, id)
                .routing(routing);

        final DeleteResponse response;
        try {
            response = delete(storage, request);
        } catch (IOException e) {
            log.error("Failed to delete from index '{}' with id '{}'", indexName, id, e);
            throw e;
        }

        if (response.getResult() == DocWriteResponse.Result.NOT_FOUND) {
            throw new IllegalArgumentException("No entity found for id: '%s'".formatted(id));
        }
        return null;
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.DELETE_DOC)
    private DeleteResponse delete(final E storage, final DeleteRequest request) throws IOException {
        return storage.client().delete(request, RequestOptions.DEFAULT);
    }
}
