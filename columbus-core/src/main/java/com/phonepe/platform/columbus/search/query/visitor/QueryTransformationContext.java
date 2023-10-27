package com.phonepe.platform.columbus.search.query.visitor;

import com.phonepe.platform.columbus.search.query.Query;
import com.phonepe.platform.columbus.search.query.QueryTransformer;
import com.phonepe.platform.columbus.visitor.impl.VisitorContext;
import lombok.experimental.SuperBuilder;

/**
 * A {@link QueryTransformer} that uses visitor to transform the given {@link Query} to the internal representation
 * used by storage.
 *
 * @param <R> represents the type used by storage.
 * @param <C> self reference, the context used by the transformers.
 */
@SuperBuilder(toBuilder = true)
public abstract class QueryTransformationContext<R, C extends QueryTransformationContext<R, C>>
        extends VisitorContext<R, Query, C> implements QueryTransformer<R> {

    @Override
    public R transform(final Query query) {
        return visit(query);
    }
}
