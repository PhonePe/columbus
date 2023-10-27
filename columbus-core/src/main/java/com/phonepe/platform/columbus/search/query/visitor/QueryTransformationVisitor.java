package com.phonepe.platform.columbus.search.query.visitor;

import com.phonepe.platform.columbus.search.query.Query;
import com.phonepe.platform.columbus.visitor.impl.EntityVisitor;

/**
 * An interface to transform a specific {@link Query} to its internal representation used by the storage.
 *
 * @param <R> represents the type used by storage.
 * @param <T> the type of the {@link Query} being transformed.
 * @param <C> the type of the context required for transformation.
 */
public interface QueryTransformationVisitor<R, T extends Query, C extends QueryTransformationContext<R, C>>
        extends EntityVisitor<R, T, Query, C> {

    @Override
    R visit(T query, C context);
}
