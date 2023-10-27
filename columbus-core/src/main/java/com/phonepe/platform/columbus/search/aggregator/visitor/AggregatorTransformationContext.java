package com.phonepe.platform.columbus.search.aggregator.visitor;

import com.phonepe.platform.columbus.search.aggregator.Aggregator;
import com.phonepe.platform.columbus.search.aggregator.AggregatorTransformer;
import com.phonepe.platform.columbus.visitor.impl.VisitorContext;
import lombok.experimental.SuperBuilder;

/**
 * An {@link AggregatorTransformer} that uses visitor to transform the given {@link Aggregator} to the internal
 * representation used by storage.
 *
 * @param <R> represents the type used by storage.
 * @param <C> self reference, the context used by the transformers.
 */
@SuperBuilder(toBuilder = true)
public abstract class AggregatorTransformationContext<R, C extends AggregatorTransformationContext<R, C>>
        extends VisitorContext<R, Aggregator, C> implements AggregatorTransformer<R> {

    @Override
    public R transform(final Aggregator aggregator) {
        return visit(aggregator);
    }
}
