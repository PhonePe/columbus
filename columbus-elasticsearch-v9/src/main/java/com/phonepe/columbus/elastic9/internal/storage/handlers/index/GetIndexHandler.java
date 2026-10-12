package com.phonepe.columbus.elastic9.internal.storage.handlers.index;

import co.elastic.clients.elasticsearch.indices.GetIndexRequest;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.json.JsonpUtils;
import co.elastic.clients.json.SimpleJsonpMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.phonepe.columbus.common.Transforms;
import com.phonepe.columbus.dao.index.IndexProperties;
import com.phonepe.columbus.elastic9.common.Constants.Operations;
import com.phonepe.columbus.elastic9.common.Constants.Properties;
import com.phonepe.columbus.elastic9.storage.ElasticClient;
import com.phonepe.columbus.elastic9.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.commands.index.GetIndexCommand;
import com.phonepe.columbus.type.EntityType;
import com.phonepe.columbus.type.Types;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Get the properties of an index.
 *
 * @param <E> the type of the Elastic client.
 */
@Slf4j
@RegistryKey(name = GetIndexCommand.COMMAND_NAME, type = GetIndexCommand.class)
public class GetIndexHandler<E extends ElasticClient<E>>
        implements ElasticStorageCommandHandler<IndexProperties, GetIndexCommand, E> {

    private static final EntityType<Map<String, Object>> MAP_TYPE = Types.forTypeReference(new TypeReference<>() {});
    private static final String PROPERTY_KEY = "properties";

    @Override
    @SneakyThrows
    public IndexProperties handle(final GetIndexCommand command, final E storage) {
        final var indexName = storage.toolkit().indexResolver().resolve(command.getIndex()).getName();
        final var request = new GetIndexRequest.Builder().index(indexName).build();
        final var serDe = storage.toolkit().serDe();

        final GetIndexResponse response;
        try {
            response = getIndex(storage, request);
        } catch (final IOException e) {
            log.error("Failed to get details of index '{}' with error", indexName, e);
            throw e;
        }

        final var indexState = response.get(indexName);

        if (indexState == null || indexState.mappings() == null || indexState.settings() == null
                || indexState.settings().index() == null) {
            log.error("Index '{}' settings does not exist", indexName);
            throw new IllegalStateException("Index '%s' settings does not exist".formatted(indexName));
        }

        final var encodedSchema = indexState.mappings().properties();
        final Map<String, Object> schema = new HashMap<>();

        final var mapper = new SimpleJsonpMapper();
        for (final var entry : encodedSchema.entrySet()) {
            final String propertyValueAsString =
                    JsonpUtils.toJsonString(entry.getValue(), mapper);
            schema.put(entry.getKey(), serDe.fromString(propertyValueAsString, MAP_TYPE));
        }

        final String indexSettingsAsJsonString =
                JsonpUtils.toJsonString(indexState.settings().index(), mapper);
        final Map<String, Object> indexSettings = serDe.fromString(indexSettingsAsJsonString, MAP_TYPE);

        return IndexProperties.builder()
                .numberOfShards(Transforms.parseInteger(indexSettings.get(Properties.SHARD_COUNT)))
                .numberOfReplicas(Transforms.parseInteger(indexSettings.get(Properties.REPLICA_COUNT)))
                .schema(Map.of(PROPERTY_KEY, schema))
                .settings(indexSettings)
                .build();
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.GET_INDEX)
    private GetIndexResponse getIndex(final E storage, final GetIndexRequest request) throws IOException {
        return storage.client().indices().get(request);
    }
}
