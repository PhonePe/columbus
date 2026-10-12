package com.phonepe.columbus.search.aggregation;

import com.phonepe.columbus.search.aggregator.Aggregator;

/**
 * An interface that resolves an {@link AggregationParser} for a given {@link Aggregator}.
 *
 * @param <R> represents the type used by storage for aggregation results.
 * @param <C> represents the additional context required for resolution.
 */
public interface AggregationParserResolver<R, C> {

    AggregationParser<R> resolve(Aggregator aggregator, C context);

    AggregationParser<R> resolve(Aggregator aggregator);
}
