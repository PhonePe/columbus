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
package com.phonepe.columbus.elastic7.internal.storage.handlers.index;

import com.phonepe.columbus.elastic7.common.Constants.Operations;
import com.phonepe.columbus.elastic7.internal.dao.utils.DaoTransforms;
import com.phonepe.columbus.elastic7.storage.ElasticClient;
import com.phonepe.columbus.elastic7.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.commands.index.CreateIndexCommand;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;

import java.io.IOException;

import static com.phonepe.columbus.common.Predicates.applyIf;

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

        final var request = new CreateIndexRequest(indexName);
        applyIf(properties.getSchema(), MapUtils::isNotEmpty, request::mapping);
        applyIf(DaoTransforms.getSettings(properties), MapUtils::isNotEmpty, request::settings);

        final var healthRequest = new ClusterHealthRequest(indexName)
                .waitForGreenStatus();

        try {
            final var response = createIndex(storage, request);
            if (!response.isShardsAcknowledged()) {
                log.warn("Shards not acknowledged for index: '{}'", indexName);
            }
            log.info("Checking index health for '{}'", indexName);
            final var health = checkIndexHealth(storage, healthRequest).getStatus();
            log.info("Index health for '{}' is {}", indexName, health.name());
        } catch (IOException e) {
            log.error("Failed to create index '{}' with error", indexName, e);
            throw e;
        }
        return null;
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.CREATE_INDEX)
    private CreateIndexResponse createIndex(final E storage, final CreateIndexRequest request) throws IOException {
        return storage.client().indices().create(request, RequestOptions.DEFAULT);
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.INDEX_HEALTH_CHECK)
    private ClusterHealthResponse checkIndexHealth(final E storage, final ClusterHealthRequest request)
            throws IOException {
        return storage.client().cluster().health(request, RequestOptions.DEFAULT);
    }
}
