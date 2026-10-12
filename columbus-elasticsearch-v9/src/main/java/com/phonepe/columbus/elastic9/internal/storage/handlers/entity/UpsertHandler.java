package com.phonepe.columbus.elastic9.internal.storage.handlers.entity;

import co.elastic.clients.elasticsearch.core.UpdateRequest;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import com.phonepe.columbus.elastic9.common.Constants.Operations;
import com.phonepe.columbus.elastic9.internal.storage.utils.ElasticClientUtils;
import com.phonepe.columbus.elastic9.internal.storage.utils.EnumTransforms;
import com.phonepe.columbus.elastic9.storage.ElasticClient;
import com.phonepe.columbus.elastic9.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.commands.entity.UpsertCommand;
import com.phonepe.columbus.storage.models.result.UpsertResult;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Upsert a value in an index in ElasticSearch.
 *
 * @param <E> the type of the Elastic client.
 */
@Slf4j
@RegistryKey(name = UpsertCommand.COMMAND_NAME, type = UpsertCommand.class)
public class UpsertHandler<T, E extends ElasticClient<E>>
        implements ElasticStorageCommandHandler<UpsertResult, UpsertCommand<T>, E> {

    @Override
    @SneakyThrows
    public UpsertResult handle(final UpsertCommand<T> command, final E storage) {
        final var toolkit = storage.toolkit();
        final var indexName = ElasticClientUtils.resolveIndexName(toolkit, command.getIndex());
        final var id = ElasticClientUtils.resolveId(toolkit, command.getId());
        final var routing = ElasticClientUtils.resolveSingleRouting(toolkit, command.getNodeId());

        final var request = new UpdateRequest.Builder<T, T>()
                .index(indexName)
                .id(id)
                .routing(routing)
                .doc(command.getValue())
                .docAsUpsert(true)
                .build();

        try {
            return EnumTransforms.toUpsertResult(upsert(storage, request).result());
        } catch (final IOException e) {
            log.error("Failed to upsert to index '{}' with id '{}'", indexName, id, e);
            throw e;
        }
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.UPSERT_DOC)
    private UpdateResponse<T> upsert(final E storage, final UpdateRequest<T, T> request) throws IOException {
        return storage.client().update(request, request.doc().getClass());
    }
}
