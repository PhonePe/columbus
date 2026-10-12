package com.phonepe.columbus.elastic7.internal.search.utils;

import com.phonepe.columbus.dao.id.Ids;
import com.phonepe.columbus.elastic7.common.Constants.Operations;
import com.phonepe.columbus.elastic7.storage.ElasticClient;
import com.phonepe.columbus.storage.commands.search.ScanCommand;
import com.phonepe.columbus.storage.models.result.StoredEntity;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;

import java.io.IOException;
import java.util.Arrays;
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
        scrollId = response.getScrollId();
        return parseResponse(response);
    }

    @SneakyThrows
    private List<StoredEntity<T>> scroll() {
        final var response = scroll(new SearchScrollRequest(scrollId).scroll(searchRequest.scroll()));
        scrollId = response.getScrollId();
        return parseResponse(response);
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.SEARCH_DOCS)
    private SearchResponse search(final SearchRequest request) throws IOException {
        log.trace("Search request: {}", request);
        return storage.client().search(request, RequestOptions.DEFAULT);
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.SCROLL_DOCS)
    private SearchResponse scroll(final SearchScrollRequest request) throws IOException {
        return storage.client().scroll(request, RequestOptions.DEFAULT);
    }

    private List<StoredEntity<T>> parseResponse(final SearchResponse response) {
        final var serDe = storage.toolkit().serDe();
        final var type = command.getEntityType();
        return Arrays.stream(response.getHits().getHits())
                .map(document -> StoredEntity.<T>builder()
                        .id(Ids.value(document.getId()))
                        .value(serDe.fromString(document.getSourceAsString(), type))
                        .build())
                .toList();
    }
}
