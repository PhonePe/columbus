package com.phonepe.columbus.search.aggregation;

import com.phonepe.columbus.search.aggregator.Aggregator;

/**
 * An interface that parses the result of an {@link Aggregator} to {@link Aggregation}.
 *
 * @param <R> represents the type used by storage for aggregation results.
 */
public interface AggregationParser<R> {

    Aggregation parse(R result);

    Aggregation parse(R result, String name);
}
