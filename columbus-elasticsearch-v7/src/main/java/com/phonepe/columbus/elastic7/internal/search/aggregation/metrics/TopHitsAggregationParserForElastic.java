package com.phonepe.columbus.elastic7.internal.search.aggregation.metrics;

import com.phonepe.columbus.elastic7.internal.search.aggregation.parser.metrics.TopHitsAggregationParser;
import com.phonepe.columbus.elastic7.search.aggregation.AggregationParserForElastic;
import com.phonepe.columbus.elastic7.search.aggregation.ElasticAggregationParserResolutionContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.aggregation.AggregationParser;
import com.phonepe.columbus.search.aggregator.metrics.TopHitsAggregator;
import org.elasticsearch.search.aggregations.Aggregation;

@RegistryKey(name = TopHitsAggregator.TYPE, type = TopHitsAggregator.class)
public class TopHitsAggregationParserForElastic<T, C extends ElasticAggregationParserResolutionContext<C>>
        implements AggregationParserForElastic<TopHitsAggregator<T>, C> {

    @Override
    public AggregationParser<Aggregation> visit(
            final TopHitsAggregator<T> aggregator,
            final Object props,
            final C context
    ) {
        return TopHitsAggregationParser.<T>builder()
                .entityType(aggregator.getEntityType())
                .serDe(context.toolkit().serDe())
                .build();
    }
}
