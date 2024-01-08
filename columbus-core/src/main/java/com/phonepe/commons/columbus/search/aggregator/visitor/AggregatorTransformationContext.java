package com.phonepe.commons.columbus.search.aggregator.visitor;

import com.phonepe.commons.columbus.search.aggregator.Aggregator;
import com.phonepe.commons.columbus.search.aggregator.AggregatorTransformer;
import com.phonepe.commons.columbus.visitor.impl.VisitorContext;
import lombok.experimental.SuperBuilder;

/**
 * An {@link AggregatorTransformer} that uses visitor to transform the given {@link Aggregator} to the internal
 * representation used by storage.
 *
 * @param <R> represents the type used by storage.
 * @param <P> represents the additional context required for transformation.
 * @param <C> self reference, the context used by the transformers.
 */
@SuperBuilder(toBuilder = true)
public abstract class AggregatorTransformationContext<R, P, C extends AggregatorTransformationContext<R, P, C>>
        extends VisitorContext<R, Aggregator, P, C> implements AggregatorTransformer<R, P> {

    @Override
    public R transform(final Aggregator aggregator, final P context) {
        return visit(aggregator, context);
    }

    @Override
    public R transform(final Aggregator aggregator) {
        return visit(aggregator);
    }
}
