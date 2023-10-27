package com.phonepe.platform.columbus.search.aggregator;

/**
 * An interface that transforms a given {@link Aggregator} to its corresponding internal representation.
 *
 * @param <R> represents the type used by storage.
 */
public interface AggregatorTransformer<R> {

    R transform(Aggregator aggregator);
}
