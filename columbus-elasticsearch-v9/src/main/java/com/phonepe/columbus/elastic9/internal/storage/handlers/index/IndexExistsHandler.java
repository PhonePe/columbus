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

import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import com.phonepe.columbus.elastic9.common.Constants.Operations;
import com.phonepe.columbus.elastic9.storage.ElasticClient;
import com.phonepe.columbus.elastic9.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.commands.index.IndexExistsCommand;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Check if an index with the given name exists.
 *
 * @param <E> the type of the Elastic client.
 */
@Slf4j
@RegistryKey(name = IndexExistsCommand.COMMAND_NAME, type = IndexExistsCommand.class)
public class IndexExistsHandler<E extends ElasticClient<E>>
        implements ElasticStorageCommandHandler<Boolean, IndexExistsCommand, E> {

    @Override
    @SneakyThrows
    public Boolean handle(final IndexExistsCommand command, final E storage) {
        final var indexName = storage.toolkit().indexResolver().resolve(command.getIndex()).getName();
        final var request = new ExistsRequest.Builder().index(indexName).build();
        try {
            return doesIndexExist(storage, request);
        } catch (final IOException e) {
            log.error("Failed to check if index '{}' exists with error", indexName, e);
            throw e;
        }
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.INDEX_EXISTS)
    private boolean doesIndexExist(final E storage, final ExistsRequest request) throws IOException {
        return storage.client().indices().exists(request).value();
    }
}
