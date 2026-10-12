package com.phonepe.columbus.elastic7.internal.storage.handlers.index;

import com.fasterxml.jackson.core.type.TypeReference;
import com.phonepe.columbus.common.Transforms;
import com.phonepe.columbus.dao.index.IndexProperties;
import com.phonepe.columbus.elastic7.common.Constants.Operations;
import com.phonepe.columbus.elastic7.common.Constants.Properties;
import com.phonepe.columbus.elastic7.storage.ElasticClient;
import com.phonepe.columbus.elastic7.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.commands.index.GetIndexCommand;
import com.phonepe.columbus.type.EntityType;
import com.phonepe.columbus.type.Types;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;

import java.io.IOException;
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

    @Override
    @SneakyThrows
    public IndexProperties handle(final GetIndexCommand command, final E storage) {
        final var indexName = storage.toolkit().indexResolver().resolve(command.getIndex()).getName();
        final var request = new GetIndexRequest(indexName);
        final var serDe = storage.toolkit().serDe();

        final GetIndexResponse response;
        try {
            response = getIndex(storage, request);
        } catch (IOException e) {
            log.error("Failed to get details of index '{}' with error", indexName, e);
            throw e;
        }

        final var schema = response.getMappings().get(indexName).sourceAsMap();
        final var encodedSettings = response.getSettings().get(indexName).getAsSettings("index").toString();
        final var settings = serDe.fromString(encodedSettings, MAP_TYPE);

        return IndexProperties.builder()
                .numberOfShards(Transforms.parseInteger(settings.get(Properties.SHARD_COUNT)))
                .numberOfReplicas(Transforms.parseInteger(settings.get(Properties.REPLICA_COUNT)))
                .schema(schema)
                .settings(settings)
                .build();
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.GET_INDEX)
    private GetIndexResponse getIndex(final E storage, final GetIndexRequest request) throws IOException {
        return storage.client().indices().get(request, RequestOptions.DEFAULT);
    }
}
