package com.phonepe.platform.columbus.search.filter.visitor;

import com.phonepe.platform.columbus.search.filter.Filter;
import com.phonepe.platform.columbus.search.query.visitor.QueryTransformationContext;
import com.phonepe.platform.columbus.search.query.visitor.QueryTransformationVisitor;

/**
 * An interface to transform a specific {@link Filter} to the internal query used by the storage.
 *
 * @param <R> represents the type used by storage.
 * @param <T> the type of the {@link Filter} being transformed.
 * @param <C> the type of the context required for transformation.
 */
public interface FilterTransformationVisitor<R, T extends Filter, C extends QueryTransformationContext<R, C>>
        extends QueryTransformationVisitor<R, T, C> {

    @Override
    R visit(final T filter, final C context);
}
