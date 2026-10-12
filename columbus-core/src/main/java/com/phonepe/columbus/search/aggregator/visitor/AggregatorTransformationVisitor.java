package com.phonepe.columbus.search.aggregator.visitor;

import com.phonepe.columbus.search.aggregator.Aggregator;
import com.phonepe.columbus.visitor.impl.EntityVisitor;

/**
 * An interface to transform a specific {@link Aggregator} to its internal representation used by the storage.
 *
 * @param <R> represents the type used by storage.
 * @param <T> the type of the {@link Aggregator} being transformed.
 * @param <P> represents the additional context required for transformation.
 * @param <C> the type of the context required for transformation.
 */
public interface AggregatorTransformationVisitor<R, T extends Aggregator, P,
        C extends AggregatorTransformationContext<R, P, C>>
        extends EntityVisitor<R, T, Aggregator, P, C> {

    @Override
    R visit(T aggregator, P props, C context);
}
