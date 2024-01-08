package com.phonepe.commons.columbus.elastic.internal.storage.handlers.entity;

import com.phonepe.commons.columbus.elastic.common.Constants.Operations;
import com.phonepe.commons.columbus.elastic.internal.storage.utils.ElasticClientUtils;
import com.phonepe.commons.columbus.elastic.internal.storage.utils.EnumTransforms;
import com.phonepe.commons.columbus.elastic.storage.ElasticClient;
import com.phonepe.commons.columbus.elastic.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.storage.commands.entity.UpsertCommand;
import com.phonepe.commons.columbus.storage.models.result.UpsertResult;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;

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
        final var serDe = toolkit.serDe();

        final var request = new UpdateRequest(indexName, id)
                .routing(routing)
                .doc(serDe.toBytes(command.getValue()), XContentType.fromMediaType(serDe.mediaType()))
                .docAsUpsert(true);

        try {
            return EnumTransforms.toUpsertResult(upsert(storage, request).getResult());
        } catch (IOException e) {
            log.error("Failed to upsert to index '{}' with id '{}'", indexName, id, e);
            throw e;
        }
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.UPSERT_DOC)
    private UpdateResponse upsert(final E storage, final UpdateRequest request) throws IOException {
        return storage.client().update(request, RequestOptions.DEFAULT);
    }
}
