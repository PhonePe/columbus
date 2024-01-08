package com.phonepe.commons.columbus.elastic.internal.storage.handlers.index;

import com.phonepe.commons.columbus.elastic.common.Constants.Operations;
import com.phonepe.commons.columbus.elastic.internal.dao.utils.DaoTransforms;
import com.phonepe.commons.columbus.elastic.storage.ElasticClient;
import com.phonepe.commons.columbus.elastic.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.storage.commands.index.UpdateIndexCommand;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.PutMappingRequest;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;

import static com.phonepe.commons.columbus.common.Predicates.applyIf;

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
        final var properties = command.getProperties();
        applyIf(properties.getSchema(), MapUtils::isNotEmpty, schema -> updateMapping(storage, indexName, schema));
        applyIf(
                DaoTransforms.getSettings(properties),
                MapUtils::isNotEmpty,
                settings -> updateSettings(storage, indexName, settings));
        return null;
    }

    private void updateMapping(final E storage, final String indexName, final Map<String, Object> mapping) {
        final var request = new PutMappingRequest(indexName).source(mapping);
        executeRequest(() -> putMapping(storage, request), indexName, "schema");
    }

    private void updateSettings(final E storage, final String indexName, final Map<String, Object> settings) {
        final var request = new UpdateSettingsRequest(indexName).settings(settings);
        executeRequest(() -> updateSettings(storage, request), indexName, "settings");
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.PUT_MAPPING)
    private AcknowledgedResponse putMapping(final E storage, final PutMappingRequest request) throws IOException {
        return storage.client().indices().putMapping(request, RequestOptions.DEFAULT);
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.UPDATE_SETTINGS)
    private AcknowledgedResponse updateSettings(final E storage, final UpdateSettingsRequest request)
            throws IOException {
        return storage.client().indices().putSettings(request, RequestOptions.DEFAULT);
    }

    @SneakyThrows
    private static void executeRequest(
            final Callable<AcknowledgedResponse> callable,
            final String indexName,
            final String operation
    ) {
        try {
            final var ack = callable.call().isAcknowledged();
            if (!ack) {
                log.warn("Update {} not acknowledged for index: '{}'", operation, indexName);
            }
        } catch (Exception e) {
            log.error("Failed to update {} for index: '{}'", operation, indexName, e);
            throw e;
        }
    }
}
