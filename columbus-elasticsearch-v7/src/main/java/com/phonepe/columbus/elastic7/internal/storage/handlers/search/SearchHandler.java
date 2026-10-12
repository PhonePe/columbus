package com.phonepe.columbus.elastic7.internal.storage.handlers.search;

import com.phonepe.columbus.elastic7.common.Constants.Operations;
import com.phonepe.columbus.elastic7.dao.node.ElasticNode;
import com.phonepe.columbus.elastic7.internal.search.utils.MiscTransforms;
import com.phonepe.columbus.elastic7.internal.storage.utils.ElasticClientUtils;
import com.phonepe.columbus.elastic7.storage.ElasticClient;
import com.phonepe.columbus.elastic7.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.aggregator.Aggregator;
import com.phonepe.columbus.storage.commands.search.SearchCommand;
import com.phonepe.columbus.storage.models.result.SearchResult;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.phonepe.columbus.common.Predicates.applyIf;
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

        final SearchResponse response;
        try {
            response = search(storage, request);
        } catch (IOException e) {
            log.error("Failed to execute search: {} with error", request, e);
            throw e;
        }

        return parseResponse(response, command, storage);
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.SEARCH_DOCS)
    private SearchResponse search(final E storage, final SearchRequest request) throws IOException {
        log.trace("Search request: {}", request);
        return storage.client().search(request, RequestOptions.DEFAULT);
    }

    private SearchRequest buildRequest(final SearchCommand<T> command, final E storage) {
        if (command.getIndexBoosts().isEmpty()) {
            throw new IllegalArgumentException("Search requires at least one index");
        }
        final var toolkit = storage.toolkit();
        final var indexBoosts = command.getIndexBoosts().entrySet().stream()
                .collect(Collectors.toMap(
                        indexBoost -> ElasticClientUtils.resolveIndexName(toolkit, indexBoost.getKey()),
                        Entry::getValue));

        final var request = new SearchRequest(
                indexBoosts.keySet().toArray(String[]::new),
                buildSearchSource(command, storage, indexBoosts));

        final var node = Optional.ofNullable(command.getNodeId())
                .map(toolkit.nodeIdResolver()::resolve);
        node.map(ElasticNode::getRouting)
                .filter(CollectionUtils::isNotEmpty)
                .map(routing -> routing.toArray(String[]::new))
                .ifPresent(request::routing);
        node.map(ElasticNode::getPreference).ifPresent(request::preference);

        if (command.isConsistentScoring()) {
            request.searchType(SearchType.DFS_QUERY_THEN_FETCH);
        }

        return request;
    }

    private SearchSourceBuilder buildSearchSource(
            final SearchCommand<T> command,
            final E storage,
            final Map<String, Float> indexBoosts
    ) {
        final var toolkit = storage.toolkit();
        final var source = new SearchSourceBuilder();

        indexBoosts.forEach(source::indexBoost);

        applyIfNotNull(command.getQuery(), toolkit.queryTransformer()::transform, source::query);

        command.getSorts().stream()
                .map(toolkit.sortTransformer()::transform)
                .forEachOrdered(source::sort);

        command.getAggregators().stream()
                .map(toolkit.aggregatorTransformer()::transform)
                .forEachOrdered(source::aggregation);

        applyIfNotNull(
                command.getCollapse(),
                collapse -> MiscTransforms.toElastic(collapse, toolkit),
                source::collapse);

        applyIfNotNull(command.getPostFilter(), toolkit.queryTransformer()::transform, source::postFilter);

        applyIfNotNull(command.getProjection(), MiscTransforms::toElastic, source::fetchSource);

        Optional.ofNullable(command.getPage())
                .map(toolkit.pageResolver()::resolve)
                .ifPresent(page -> {
                    applyIfNotNull(page.getSize(), source::size);
                    applyIfNotNull(page.getFrom(), source::from);
                    applyIf(
                            page.getAfterKeys(),
                            CollectionUtils::isNotEmpty,
                            keys -> source.searchAfter(keys.toArray()));
                });

        return source;
    }

    private SearchResult<T> parseResponse(
            final SearchResponse response,
            final SearchCommand<T> command,
            final E storage
    ) {
        final var toolkit = storage.toolkit();

        final var builder = SearchResult.<T>builder()
                .result(MiscTransforms.fromElastic(
                        response.getHits(),
                        command.getEntityType(),
                        toolkit.serDe(),
                        command.getCollapse()));

        if (response.getAggregations() != null) {
            final var parsers = command.getAggregators().stream()
                    .collect(Collectors.toMap(Aggregator::getName, toolkit.aggregationParserResolver()::resolve));

            response.getAggregations().asList().stream()
                    .map(aggregation -> parsers.get(aggregation.getName()).parse(aggregation))
                    .forEach(aggregation -> builder.aggregation(aggregation.getName(), aggregation));
        }

        return builder.build();
    }
}
