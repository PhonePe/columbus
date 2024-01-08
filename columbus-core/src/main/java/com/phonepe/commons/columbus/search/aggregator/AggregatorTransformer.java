package com.phonepe.commons.columbus.search.aggregator;

/**
 * An interface that transforms a given {@link Aggregator} to its corresponding internal representation.
 *
 * @param <R> represents the type used by storage.
 * @param <C> represents the additional context required for transformation.
 */
public interface AggregatorTransformer<R, C> {

    R transform(Aggregator aggregator, C context);

    R transform(Aggregator aggregator);
}
