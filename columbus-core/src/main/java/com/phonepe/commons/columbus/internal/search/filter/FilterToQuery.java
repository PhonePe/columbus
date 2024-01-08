package com.phonepe.commons.columbus.internal.search.filter;

import com.phonepe.commons.columbus.search.filter.Filter;
import com.phonepe.commons.columbus.search.filter.visitor.FilterTransformationVisitor;
import com.phonepe.commons.columbus.search.query.BaseQuery.BaseQueryBuilder;
import com.phonepe.commons.columbus.search.query.Query;
import com.phonepe.commons.columbus.search.query.visitor.QueryTransformationContext;

/**
 * A {@link FilterTransformationVisitor} that first transforms the {@link Filter} to a {@link Query} and then uses its
 * visitor to transform to the internal representation. This allows filters to work across storages as long as the
 * queries they transform to are supported by the storage.
 *
 * @param <R> represents the type used by storage.
 * @param <T> the type of the {@link Filter} being transformed.
 * @param <P> represents the additional context required for transformation.
 * @param <C> the type of the context required for transformation.
 */
public abstract class FilterToQuery<R, T extends Filter, P, C extends QueryTransformationContext<R, P, C>>
        implements FilterTransformationVisitor<R, T, P, C> {

    @Override
    public R visit(final T filter, final P props, final C context) {
        return context.transform(toQueryBuilder(filter, props).build(), props);
    }

    @SuppressWarnings("java:S1452")
    protected abstract BaseQueryBuilder<?, ?> toQueryBuilder(T filter, final P props);
}
