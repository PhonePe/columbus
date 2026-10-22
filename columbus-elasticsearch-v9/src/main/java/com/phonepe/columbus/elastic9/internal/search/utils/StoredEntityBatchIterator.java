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
package com.phonepe.columbus.elastic9.internal.search.utils;

import co.elastic.clients.elasticsearch.core.ScrollRequest;
import co.elastic.clients.elasticsearch.core.ScrollResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.ResponseBody;
import com.phonepe.columbus.dao.id.Ids;
import com.phonepe.columbus.elastic9.common.Constants.Operations;
import com.phonepe.columbus.elastic9.storage.ElasticClient;
import com.phonepe.columbus.storage.commands.search.ScanCommand;
import com.phonepe.columbus.storage.models.result.StoredEntity;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
public class StoredEntityBatchIterator<T, E extends ElasticClient<E>> implements Iterator<List<StoredEntity<T>>> {

    @NonNull
    private final SearchRequest searchRequest;
    @NonNull
    private final ScanCommand<T> command;
    @NonNull
    private final E storage;

    private List<StoredEntity<T>> page;
    private String scrollId;
    private boolean loadNext;

    @Override
    public boolean hasNext() {
        if (page == null) {
            page = search();
        } else if (loadNext) {
            page = scroll();
        }
        loadNext = false;
        return !page.isEmpty();
    }

    @Override
    public List<StoredEntity<T>> next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Scanner has reached the end");
        }
        loadNext = true;
        return page;
    }

    @SneakyThrows
    private List<StoredEntity<T>> search() {
        final var response = search(searchRequest);
        scrollId = response.scrollId();
        return parseResponse(response);
    }

    @SneakyThrows
    private List<StoredEntity<T>> scroll() {
        final var scrollRequest = new ScrollRequest.Builder()
                .scrollId(scrollId)
                .scroll(searchRequest.scroll())
                .build();
        final var response = scroll(scrollRequest);
        scrollId = response.scrollId();
        return parseResponse(response);
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.SEARCH_DOCS)
    private SearchResponse<T> search(final SearchRequest request) throws IOException {
        log.trace("Search request: {}", request);
        final var typeResolver = storage.toolkit().typeResolver();
        final var type = typeResolver.toRawType(command.getEntityType());
        return storage.client().search(request, type);
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.SCROLL_DOCS)
    private ScrollResponse<T> scroll(final ScrollRequest request) throws IOException {
        final var typeResolver = storage.toolkit().typeResolver();
        final var type = typeResolver.toRawType(command.getEntityType());
        return storage.client().scroll(request, type);
    }

    private List<StoredEntity<T>> parseResponse(final ResponseBody<T> response) {
        return response.hits().hits().stream()
                .map(document -> StoredEntity.<T>builder()
                        .id(Ids.value(document.id()))
                        .value(document.source())
                        .build())
                .toList();
    }
}
