package com.phonepe.columbus.search.query.visitor;

import com.phonepe.columbus.search.query.Query;
import com.phonepe.columbus.search.query.QueryTransformer;
import com.phonepe.columbus.visitor.impl.VisitorContext;
import lombok.experimental.SuperBuilder;

/**
 * A {@link QueryTransformer} that uses visitor to transform the given {@link Query} to the internal representation
 * used by storage.
 *
 * @param <R> represents the type used by storage.
 * @param <P> represents the additional context required for transformation.
 * @param <C> self reference, the context used by the transformers.
 */
@SuperBuilder(toBuilder = true)
public abstract class QueryTransformationContext<R, P, C extends QueryTransformationContext<R, P, C>>
        extends VisitorContext<R, Query, P, C> implements QueryTransformer<R, P> {

    @Override
    public R transform(final Query query, final P context) {
        return visit(query, context);
    }

    @Override
    public R transform(final Query query) {
        return visit(query);
    }
}
