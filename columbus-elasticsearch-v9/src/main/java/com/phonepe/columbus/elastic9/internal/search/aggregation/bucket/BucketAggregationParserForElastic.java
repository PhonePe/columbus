package com.phonepe.columbus.elastic9.internal.search.aggregation.bucket;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import com.phonepe.columbus.elastic9.search.aggregation.AggregationParserForElastic;
import com.phonepe.columbus.elastic9.search.aggregation.ElasticAggregationParserResolutionContext;
import com.phonepe.columbus.search.aggregation.AggregationParser;
import com.phonepe.columbus.search.aggregator.Aggregator;
import com.phonepe.columbus.search.aggregator.bucket.BucketAggregator;

import java.util.Map;
import java.util.stream.Collectors;

public abstract class BucketAggregationParserForElastic<T extends BucketAggregator,
        C extends ElasticAggregationParserResolutionContext<C>>
        implements AggregationParserForElastic<T, C> {

    @Override
    public AggregationParser<Aggregate> visit(final T aggregator, final Object props, final C context) {
        final var subAggregationParsers = aggregator.getSubAggregators().stream()
                .collect(Collectors.toMap(Aggregator::getName, subAggregator -> context.resolve(subAggregator, props)));
        return createParser(aggregator, subAggregationParsers, props, context);
    }

    protected abstract AggregationParser<Aggregate> createParser(
            final T aggregator,
            final Map<String, AggregationParser<Aggregate>> subAggregationParsers,
            final Object props,
            final C context
    );
}
