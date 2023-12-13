package com.phonepe.platform.columbus.elastic.search.aggregation;

import com.phonepe.platform.columbus.search.aggregation.AggregationParser;
import com.phonepe.platform.columbus.search.aggregation.visitor.AggregationParserResolutionVisitor;
import com.phonepe.platform.columbus.search.aggregator.Aggregator;
import org.elasticsearch.search.aggregations.Aggregation;

/**
 * An interface to resolve an {@link AggregationParser
 * AggregationParser} for an {@link Aggregator}'s result.
 *
 * @param <T> the type of the {@link Aggregator} whose parser is to be resolved.
 * @param <C> the type of the context required for resolution.
 */
public interface AggregationParserForElastic<T extends Aggregator,
        C extends ElasticAggregationParserResolutionContext<C>>
        extends AggregationParserResolutionVisitor<Aggregation, T, Object, C> {
}
