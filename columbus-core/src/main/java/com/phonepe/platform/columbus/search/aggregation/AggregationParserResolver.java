package com.phonepe.platform.columbus.search.aggregation;

import com.phonepe.platform.columbus.search.aggregator.Aggregator;

/**
 * An interface that resolves an {@link AggregationParser} for a given {@link Aggregator}.
 *
 * @param <R> represents the type used by storage for aggregation results.
 */
public interface AggregationParserResolver<R> {

    AggregationParser<R> resolve(Aggregator aggregator);
}
