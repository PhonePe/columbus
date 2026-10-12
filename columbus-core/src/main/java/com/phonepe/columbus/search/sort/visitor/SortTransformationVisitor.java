package com.phonepe.columbus.search.sort.visitor;

import com.phonepe.columbus.search.sort.Sort;
import com.phonepe.columbus.visitor.impl.EntityVisitor;

/**
 * An interface to transform a specific {@link Sort} to its internal representation used by the storage.
 *
 * @param <R> represents the type used by storage.
 * @param <T> the type of the {@link Sort} being transformed.
 * @param <P> represents the additional context required for transformation.
 * @param <C> the type of the context required for transformation.
 */
public interface SortTransformationVisitor<R, T extends Sort, P, C extends SortTransformationContext<R, P, C>>
        extends EntityVisitor<R, T, Sort, P, C> {

    @Override
    R visit(T sort, P props, C context);
}
