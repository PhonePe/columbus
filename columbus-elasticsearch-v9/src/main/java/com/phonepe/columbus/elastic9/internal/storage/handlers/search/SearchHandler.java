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
package com.phonepe.columbus.elastic9.internal.storage.handlers.search;

import co.elastic.clients.elasticsearch._types.SearchType;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.phonepe.columbus.elastic9.common.Constants.Operations;
import com.phonepe.columbus.elastic9.internal.search.utils.MiscTransforms;
import com.phonepe.columbus.elastic9.storage.ElasticClient;
import com.phonepe.columbus.elastic9.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.commands.search.SearchCommand;
import com.phonepe.columbus.storage.models.result.SearchResult;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

/**
 * Search entities in an ElasticSearch index.
 *
 * @param <E> the type of the Elastic client.
 */
@Slf4j
@RegistryKey(name = SearchCommand.COMMAND_NAME, type = SearchCommand.class)
public class SearchHandler<T, E extends ElasticClient<E>>
        implements ElasticStorageCommandHandler<SearchResult<T>, SearchCommand<T>, E> {

    @Override
    @SneakyThrows
    public SearchResult<T> handle(final SearchCommand<T> command, final E storage) {
        final var request = buildRequest(command, storage);

        final SearchResponse<T> response;
        try {
            response = search(storage, request, command);
        } catch (final IOException e) {
            log.error("Failed to execute search: {} with error", request, e);
            throw e;
        }

        return MiscTransforms.fromElastic(response, command, storage.toolkit());
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.SEARCH_DOCS)
    private SearchResponse<T> search(final E storage, final SearchRequest request, final SearchCommand<T> command)
            throws IOException {
        log.trace("Search request: {}", request);
        final var typeResolver = storage.toolkit().typeResolver();
        final var type = typeResolver.toRawType(command.getEntityType());
        return storage.client().search(request, type);
    }

    private SearchRequest buildRequest(final SearchCommand<T> command, final E storage) {
        if (command.getIndexBoosts().isEmpty()) {
            throw new IllegalArgumentException("Search requires at least one index");
        }
        final var toolkit = storage.toolkit();
        final var searchRequest = MiscTransforms.toElastic(command, toolkit);

        applyIfNotNull(command.getQuery(), toolkit.queryTransformer()::transform, searchRequest::query);

        command.getSorts().stream()
                .map(toolkit.sortTransformer()::transform)
                .forEachOrdered(searchRequest::sort);

        if (command.isConsistentScoring()) {
            searchRequest.searchType(SearchType.DfsQueryThenFetch);
        }

        return searchRequest.build();
    }
}
