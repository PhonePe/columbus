package com.phonepe.commons.columbus.elastic9.internal.storage.handlers.search;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SearchType;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.util.NamedValue;
import com.phonepe.commons.columbus.elastic9.common.Constants.Operations;
import com.phonepe.commons.columbus.elastic9.dao.node.ElasticNode;
import com.phonepe.commons.columbus.elastic9.internal.search.utils.MiscTransforms;
import com.phonepe.commons.columbus.elastic9.internal.storage.utils.ElasticClientUtils;
import com.phonepe.commons.columbus.elastic9.storage.ElasticClient;
import com.phonepe.commons.columbus.elastic9.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.aggregator.Aggregator;
import com.phonepe.commons.columbus.storage.commands.search.SearchCommand;
import com.phonepe.commons.columbus.storage.models.result.SearchResult;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

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

        return parseResponse(response, command, storage);
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
        final var searchRequest = new SearchRequest.Builder();

        command.getIndexBoosts().forEach((key, value) -> {
            final var indexName = ElasticClientUtils.resolveIndexName(toolkit, key);
            searchRequest
                    .index(indexName)
                    .indicesBoost(NamedValue.of(indexName, (double) value));
        });

        applyIfNotNull(command.getQuery(), toolkit.queryTransformer()::transform, searchRequest::query);

        command.getSorts().stream()
                .map(toolkit.sortTransformer()::transform)
                .forEachOrdered(searchRequest::sort);

        command.getAggregators().forEach(aggregator ->
                searchRequest.aggregations(aggregator.getName(), toolkit.aggregatorTransformer().transform(aggregator)));

        applyIfNotNull(
                command.getCollapse(),
                collapse -> MiscTransforms.toElastic(collapse, toolkit),
                searchRequest::collapse);

        applyIfNotNull(command.getPostFilter(), toolkit.queryTransformer()::transform, searchRequest::postFilter);

        applyIfNotNull(command.getProjection(), MiscTransforms::toElastic, searchRequest::source);

        Optional.ofNullable(command.getPage())
                .map(toolkit.pageResolver()::resolve)
                .ifPresent(page -> {
                    applyIfNotNull(page.getSize(), searchRequest::size);
                    applyIfNotNull(page.getFrom(), searchRequest::from);
                    applyIfNotNull(page.getAfterKeys(),
                            keys -> keys.stream().map(FieldValue::of).forEach(searchRequest::searchAfter));
                });

        final var node = Optional.ofNullable(command.getNodeId())
                .map(toolkit.nodeIdResolver()::resolve);

        node.map(ElasticNode::getRouting)
                .filter(CollectionUtils::isNotEmpty)
                .map(routing -> String.join( ",", routing))
                .ifPresent(searchRequest::routing);
        node.map(ElasticNode::getPreference).ifPresent(searchRequest::preference);

        if (command.isConsistentScoring()) {
            searchRequest.searchType(SearchType.DfsQueryThenFetch);
        }

        return searchRequest.build();
    }

    private SearchResult<T> parseResponse(
            final SearchResponse<T> response,
            final SearchCommand<T> command,
            final E storage
    ) {
        final var toolkit = storage.toolkit();

        final var builder = SearchResult.<T>builder()
                .result(MiscTransforms.fromElastic(
                        response.hits(),
                        toolkit.serDe(),
                        command.getCollapse()));

        if (response.aggregations() != null) {
            final var parsers = command.getAggregators().stream()
                    .collect(Collectors.toMap(Aggregator::getName, toolkit.aggregationParserResolver()::resolve));

            final Map<String, Aggregate> aggregates = response.aggregations();
            aggregates.forEach((key, aggregate) ->
                    builder.aggregation(key, parsers.get(key).parse(aggregate, key)));
        }

        return builder.build();
    }
}
