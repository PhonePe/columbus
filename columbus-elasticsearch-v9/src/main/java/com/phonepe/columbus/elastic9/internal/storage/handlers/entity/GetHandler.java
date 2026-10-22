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
package com.phonepe.columbus.elastic9.internal.storage.handlers.entity;

import co.elastic.clients.elasticsearch.core.GetRequest;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.search.SourceConfigParam;
import com.phonepe.columbus.elastic9.common.Constants.Operations;
import com.phonepe.columbus.elastic9.internal.storage.utils.ElasticClientUtils;
import com.phonepe.columbus.elastic9.internal.storage.utils.EntityTransforms;
import com.phonepe.columbus.elastic9.storage.ElasticClient;
import com.phonepe.columbus.elastic9.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.commands.entity.GetCommand;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Optional;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

/**
 * Get a value from an ElasticSearch index by id.
 *
 * @param <E> the type of the Elastic client.
 */
@Slf4j
@RegistryKey(name = GetCommand.COMMAND_NAME, type = GetCommand.class)
public class GetHandler<T, E extends ElasticClient<E>>
        implements ElasticStorageCommandHandler<Optional<T>, GetCommand<T>, E> {

    @Override
    @SneakyThrows
    public Optional<T> handle(final GetCommand<T> command, final E storage) {
        final var toolkit = storage.toolkit();
        final var indexName = ElasticClientUtils.resolveIndexName(toolkit, command.getIndex());
        final var id = ElasticClientUtils.resolveId(toolkit, command.getId());
        final var node = ElasticClientUtils.resolveNodeId(toolkit, command.getNodeId());
        final var typeResolver = storage.toolkit().typeResolver();
        final var type = typeResolver.toRawType(command.getEntityType());

        final var request = new GetRequest.Builder()
                .index(indexName)
                .id(id)
                .routing(EntityTransforms.getSingleRouting(node))
                .preference(node.getPreference());

        applyIfNotNull(command.getProjection(), projection -> {
            if (projection.disabled()) {
                request.source(new SourceConfigParam.Builder().fetch(false).build());
            }

            applyIfNotNull(projection.getExcludeFields(), request::sourceExcludes);
            applyIfNotNull(projection.getIncludeFields(), request::sourceIncludes);
        });

        final GetResponse<T> response;
        try {
            response = getById(storage, request.build(), type);
        } catch (final IOException e) {
            log.error("Failed to get from index '{}' with id '{}'", indexName, id, e);
            throw e;
        }
        if (response.found() && response.source() != null) {
            return Optional.of(response.source());
        }
        return Optional.empty();
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.GET_DOC)
    private GetResponse<T> getById(final E storage, final GetRequest request,
                                        final Type type) throws IOException {
        return storage.client().get(request, type);
    }
}
