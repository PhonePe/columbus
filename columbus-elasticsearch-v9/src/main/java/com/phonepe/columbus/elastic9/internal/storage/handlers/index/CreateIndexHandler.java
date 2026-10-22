/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phonepe.columbus.elastic9.internal.storage.handlers.index;

import co.elastic.clients.elasticsearch._types.HealthStatus;
import co.elastic.clients.elasticsearch.cluster.HealthRequest;
import co.elastic.clients.elasticsearch.cluster.HealthResponse;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import com.phonepe.columbus.elastic9.common.Constants.Operations;
import com.phonepe.columbus.elastic9.internal.dao.utils.DaoTransforms;
import com.phonepe.columbus.elastic9.storage.ElasticClient;
import com.phonepe.columbus.elastic9.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.commands.index.CreateIndexCommand;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

/**
 * Create a new index in the ElasticSearch cluster, with the given name and properties.
 *
 * @param <E> the type of the Elastic client.
 */
@Slf4j
@RegistryKey(name = CreateIndexCommand.COMMAND_NAME, type = CreateIndexCommand.class)
public class CreateIndexHandler<E extends ElasticClient<E>>
        implements ElasticStorageCommandHandler<Void, CreateIndexCommand, E> {

    @Override
    @SneakyThrows
    public Void handle(final CreateIndexCommand command, final E storage) {
        final var properties = command.getProperties();
        final var indexName = storage.toolkit().indexResolver().resolve(command.getIndex()).getName();
        final var serDe = storage.toolkit().serDe();

        final var request = new CreateIndexRequest.Builder().index(indexName);
        applyIfNotNull(DaoTransforms.getTypeMappings(properties.getSchema(), serDe), request::mappings);
        applyIfNotNull(DaoTransforms.getSettings(properties), request::settings);

        final var healthRequest = new HealthRequest.Builder().index(indexName)
                .waitForStatus(HealthStatus.Green)
                .build();

        try {
            final var response = createIndex(storage, request.build());
            if (!response.shardsAcknowledged()) {
                log.warn("Shards not acknowledged for index: '{}'", indexName);
            }
            log.info("Checking index health for '{}'", indexName);
            final var health = checkIndexHealth(storage, healthRequest).status();
            log.info("Index health for '{}' is {}", indexName, health.name());
        } catch (final IOException e) {
            log.error("Failed to create index '{}' with error", indexName, e);
            throw e;
        }
        return null;
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.CREATE_INDEX)
    private CreateIndexResponse createIndex(final E storage, final CreateIndexRequest request) throws IOException {
        return storage.client().indices().create(request);
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.INDEX_HEALTH_CHECK)
    private HealthResponse checkIndexHealth(final E storage, final HealthRequest request)
            throws IOException {
        return storage.client().cluster().health(request);
    }
}
