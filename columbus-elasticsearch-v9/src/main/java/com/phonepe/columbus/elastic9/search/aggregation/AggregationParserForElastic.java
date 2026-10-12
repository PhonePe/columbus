package com.phonepe.columbus.elastic9.search.aggregation;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import com.phonepe.columbus.search.aggregation.AggregationParser;
import com.phonepe.columbus.search.aggregation.visitor.AggregationParserResolutionVisitor;
import com.phonepe.columbus.search.aggregator.Aggregator;

/**
 * An interface to resolve an {@link AggregationParser
 * AggregationParser} for an {@link Aggregator}'s result.
 *
 * @param <T> the type of the {@link Aggregator} whose parser is to be resolved.
 * @param <C> the type of the context required for resolution.
 */
public interface AggregationParserForElastic<T extends Aggregator,
        C extends ElasticAggregationParserResolutionContext<C>>
        extends AggregationParserResolutionVisitor<Aggregate, T, Object, C> {
}
