package com.phonepe.platform.columbus.search.query.visitor;

import com.phonepe.platform.columbus.search.query.Query;
import com.phonepe.platform.columbus.visitor.impl.EntityVisitor;

/**
 * An interface to transform a specific {@link Query} to its internal representation used by the storage.
 *
 * @param <R> represents the type used by storage.
 * @param <T> the type of the {@link Query} being transformed.
 * @param <P> represents the additional context required for transformation.
 * @param <C> the type of the context required for transformation.
 */
public interface QueryTransformationVisitor<R, T extends Query, P, C extends QueryTransformationContext<R, P, C>>
        extends EntityVisitor<R, T, Query, P, C> {

    @Override
    R visit(T query, P props, C context);
}
