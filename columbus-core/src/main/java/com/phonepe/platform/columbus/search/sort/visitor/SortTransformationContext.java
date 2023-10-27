package com.phonepe.platform.columbus.search.sort.visitor;

import com.phonepe.platform.columbus.search.sort.Sort;
import com.phonepe.platform.columbus.search.sort.SortTransformer;
import com.phonepe.platform.columbus.visitor.impl.VisitorContext;
import lombok.experimental.SuperBuilder;

/**
 * A {@link SortTransformer} that uses visitor to transform the given {@link Sort} to the internal representation
 * used by storage.
 *
 * @param <R> represents the type used by storage.
 * @param <C> self reference, the context used by the transformers.
 */
@SuperBuilder(toBuilder = true)
public abstract class SortTransformationContext<R, C extends SortTransformationContext<R, C>>
        extends VisitorContext<R, Sort, C> implements SortTransformer<R> {

    @Override
    public R transform(final Sort sort) {
        return visit(sort);
    }
}
