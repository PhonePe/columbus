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
package com.phonepe.columbus.elastic7.internal.storage.handlers.entity;

import com.phonepe.columbus.elastic7.common.Constants.Operations;
import com.phonepe.columbus.elastic7.internal.search.utils.MiscTransforms;
import com.phonepe.columbus.elastic7.internal.storage.utils.ElasticClientUtils;
import com.phonepe.columbus.elastic7.internal.storage.utils.EntityTransforms;
import com.phonepe.columbus.elastic7.storage.ElasticClient;
import com.phonepe.columbus.elastic7.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.commands.entity.GetCommand;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;

import java.io.IOException;
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
        final var serDe = toolkit.serDe();

        final var request = new GetRequest(indexName, id)
                .routing(EntityTransforms.getSingleRouting(node))
                .preference(node.getPreference());

        applyIfNotNull(command.getProjection(), MiscTransforms::toElastic, request::fetchSourceContext);

        final GetResponse response;
        try {
            response = getById(storage, request);
        } catch (IOException e) {
            log.error("Failed to get from index '{}' with id '{}'", indexName, id, e);
            throw e;
        }
        if (response.isExists()) {
            return Optional.of(serDe.fromBytes(response.getSourceAsBytes(), command.getEntityType()));
        }
        return Optional.empty();
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.GET_DOC)
    private GetResponse getById(final E storage, final GetRequest request) throws IOException {
        return storage.client().get(request, RequestOptions.DEFAULT);
    }
}
