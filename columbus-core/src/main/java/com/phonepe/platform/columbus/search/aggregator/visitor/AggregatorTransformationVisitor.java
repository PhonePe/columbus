package com.phonepe.platform.columbus.search.aggregator.visitor;

import com.phonepe.platform.columbus.search.aggregator.Aggregator;
import com.phonepe.platform.columbus.visitor.impl.EntityVisitor;

/**
 * An interface to transform a specific {@link Aggregator} to its internal representation used by the storage.
 *
 * @param <R> represents the type used by storage.
 * @param <T> the type of the {@link Aggregator} being transformed.
 * @param <C> the type of the context required for transformation.
 */
public interface AggregatorTransformationVisitor<R, T extends Aggregator,
        C extends AggregatorTransformationContext<R, C>>
        extends EntityVisitor<R, T, Aggregator, C> {

    @Override
    R visit(T aggregator, C context);
}
