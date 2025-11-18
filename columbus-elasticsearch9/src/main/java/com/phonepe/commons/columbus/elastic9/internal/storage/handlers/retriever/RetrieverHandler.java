package com.phonepe.commons.columbus.elastic9.internal.storage.handlers.retriever;

import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.phonepe.commons.columbus.elastic9.common.Constants.Operations;
import com.phonepe.commons.columbus.elastic9.internal.search.utils.MiscTransforms;
import com.phonepe.commons.columbus.elastic9.storage.ElasticClient;
import com.phonepe.commons.columbus.elastic9.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.storage.commands.retriever.RetrieverCommand;
import com.phonepe.commons.columbus.storage.models.result.SearchResult;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

/**
 * Retrieve entities in an ElasticSearch index.
 *
 * @param <E> the type of the Elastic client.
 */
@Slf4j
@RegistryKey(name = RetrieverCommand.COMMAND_NAME, type = RetrieverCommand.class)
public class RetrieverHandler<T, E extends ElasticClient<E>>
        implements ElasticStorageCommandHandler<SearchResult<T>, RetrieverCommand<T>, E> {

    @Override
    @SneakyThrows
    public SearchResult<T> handle(final RetrieverCommand<T> command, final E storage) {
        final var request = buildRequest(command, storage);

        final SearchResponse response;
        try {
            response = search(storage, request, command);
        } catch (final IOException e) {
            log.error("Failed to execute search: {} with error", request, e);
            throw e;
        }

        return MiscTransforms.fromElastic(response, command, storage.toolkit());
    }

    private SearchRequest buildRequest(final RetrieverCommand<T> command, final E storage) {
        if (command.getIndexBoosts().isEmpty()) {
            throw new IllegalArgumentException("Search requires at least one index");
        }
        final var toolkit = storage.toolkit();
        final var searchRequest = MiscTransforms.toElastic(command, toolkit);

        applyIfNotNull(command.getRetriever(), toolkit.retrieverTransformer()::transform, searchRequest::retriever);

        return searchRequest.build();
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.RETRIEVE_DOCS)
    private SearchResponse<T> search(final E storage, final SearchRequest request, final RetrieverCommand<T> command)
            throws IOException {
        log.trace("Retrieve request: {}", request);
        final var typeResolver = storage.toolkit().typeResolver();
        final var type = typeResolver.toJavaType(command.getEntityType()).getType().getType();
        return storage.client().search(request, type);
    }
}
