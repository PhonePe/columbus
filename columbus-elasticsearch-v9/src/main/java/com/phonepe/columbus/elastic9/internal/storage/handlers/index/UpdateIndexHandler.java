package com.phonepe.columbus.elastic9.internal.storage.handlers.index;

import co.elastic.clients.elasticsearch._types.AcknowledgedResponse;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch.indices.IndexSettings;
import co.elastic.clients.elasticsearch.indices.PutIndicesSettingsRequest;
import co.elastic.clients.elasticsearch.indices.PutIndicesSettingsResponse;
import co.elastic.clients.elasticsearch.indices.PutMappingRequest;
import co.elastic.clients.elasticsearch.indices.PutMappingResponse;
import com.phonepe.columbus.elastic9.common.Constants.Operations;
import com.phonepe.columbus.elastic9.internal.dao.utils.DaoTransforms;
import com.phonepe.columbus.elastic9.storage.ElasticClient;
import com.phonepe.columbus.elastic9.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.commands.index.UpdateIndexCommand;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

/**
 * Update the properties of an index.
 *
 * @param <E> the type of the Elastic client.
 */
@Slf4j
@RegistryKey(name = UpdateIndexCommand.COMMAND_NAME, type = UpdateIndexCommand.class)
public class UpdateIndexHandler<E extends ElasticClient<E>>
        implements ElasticStorageCommandHandler<Void, UpdateIndexCommand, E> {

    @Override
    @SneakyThrows
    public Void handle(final UpdateIndexCommand command, final E storage) {
        final var indexName = storage.toolkit().indexResolver().resolve(command.getIndex()).getName();
        final var serDe = storage.toolkit().serDe();
        final var properties = command.getProperties();
        applyIfNotNull(properties.getSchema(),
                schema -> updateMapping(storage,
                indexName, DaoTransforms.getProperties(schema, serDe)));
        applyIfNotNull(properties,
                value -> updateSettings(storage, indexName, DaoTransforms.getSettings(value)));
        return null;
    }

    private void updateMapping(final E storage, final String indexName, final Map<String, Property> mapping) {
        final var request = new PutMappingRequest.Builder().index(indexName).properties(mapping).build();
        executeRequest(() -> putMapping(storage, request), indexName, "schema");
    }

    private void updateSettings(final E storage, final String indexName, final IndexSettings settings) {
        final var request = new PutIndicesSettingsRequest.Builder()
                .index(indexName)
                .settings(settings)
                .build();
        executeRequest(() -> updateSettings(storage, request), indexName, "settings");
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.PUT_MAPPING)
    private PutMappingResponse putMapping(final E storage, final PutMappingRequest request) throws IOException {
        return storage.client().indices().putMapping(request);
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.UPDATE_SETTINGS)
    private PutIndicesSettingsResponse updateSettings(final E storage, final PutIndicesSettingsRequest request)
            throws IOException {
        return storage.client().indices().putSettings(request);
    }

    @SneakyThrows
    private static void executeRequest(
            final Callable<AcknowledgedResponse> callable,
            final String indexName,
            final String operation
    ) {
        try {
            final var ack = callable.call().acknowledged();
            if (!ack) {
                log.warn("Update {} not acknowledged for index: '{}'", operation, indexName);
            }
        } catch (final Exception e) {
            log.error("Failed to update {} for index: '{}'", operation, indexName, e);
            throw e;
        }
    }
}
