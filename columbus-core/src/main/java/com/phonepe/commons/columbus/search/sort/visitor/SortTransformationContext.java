package com.phonepe.commons.columbus.search.sort.visitor;

import com.phonepe.commons.columbus.search.sort.Sort;
import com.phonepe.commons.columbus.search.sort.SortTransformer;
import com.phonepe.commons.columbus.visitor.impl.VisitorContext;
import lombok.experimental.SuperBuilder;

/**
 * A {@link SortTransformer} that uses visitor to transform the given {@link Sort} to the internal representation
 * used by storage.
 *
 * @param <R> represents the type used by storage.
 * @param <P> represents the additional context required for transformation.
 * @param <C> self reference, the context used by the transformers.
 */
@SuperBuilder(toBuilder = true)
public abstract class SortTransformationContext<R, P, C extends SortTransformationContext<R, P, C>>
        extends VisitorContext<R, Sort, P, C> implements SortTransformer<R, P> {

    @Override
    public R transform(final Sort sort, final P context) {
        return visit(sort, context);
    }

    @Override
    public R transform(final Sort sort) {
        return visit(sort);
    }
}
