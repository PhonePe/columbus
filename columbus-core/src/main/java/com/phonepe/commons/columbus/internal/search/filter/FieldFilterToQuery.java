package com.phonepe.commons.columbus.internal.search.filter;

import com.phonepe.commons.columbus.search.filter.FieldFilter;
import com.phonepe.commons.columbus.search.query.AbstractQuery.AbstractQueryBuilder;
import com.phonepe.commons.columbus.search.query.FieldQuery.FieldQueryBuilder;
import com.phonepe.commons.columbus.search.query.visitor.QueryTransformationContext;

public abstract class FieldFilterToQuery<R, T extends FieldFilter, P, C extends QueryTransformationContext<R, P, C>>
        extends FilterToQuery<R, T, P, C> {

    @Override
    protected AbstractQueryBuilder<?, ?> toQueryBuilder(final T filter, final P props) {
        return toFieldQueryBuilder(filter, props)
                .field(filter.getField());
    }

    @SuppressWarnings("java:S1452")
    protected abstract FieldQueryBuilder<?, ?> toFieldQueryBuilder(T filter, final P props);
}
