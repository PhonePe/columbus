package com.phonepe.platform.columbus.search.sort.visitor;

import com.phonepe.platform.columbus.search.sort.Sort;
import com.phonepe.platform.columbus.visitor.impl.EntityVisitor;

/**
 * An interface to transform a specific {@link Sort} to its internal representation used by the storage.
 *
 * @param <R> represents the type used by storage.
 * @param <T> the type of the {@link Sort} being transformed.
 * @param <C> the type of the context required for transformation.
 */
public interface SortTransformationVisitor<R, T extends Sort, C extends SortTransformationContext<R, C>>
        extends EntityVisitor<R, T, Sort, C> {

    @Override
    R visit(T sort, C context);
}
