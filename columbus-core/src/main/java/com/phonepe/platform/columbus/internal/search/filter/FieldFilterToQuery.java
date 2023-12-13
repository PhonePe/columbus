package com.phonepe.platform.columbus.internal.search.filter;

import com.phonepe.platform.columbus.search.filter.FieldFilter;
import com.phonepe.platform.columbus.search.query.BaseQuery.BaseQueryBuilder;
import com.phonepe.platform.columbus.search.query.FieldQuery.FieldQueryBuilder;
import com.phonepe.platform.columbus.search.query.visitor.QueryTransformationContext;

public abstract class FieldFilterToQuery<R, T extends FieldFilter, P, C extends QueryTransformationContext<R, P, C>>
        extends FilterToQuery<R, T, P, C> {

    @Override
    protected BaseQueryBuilder<?, ?> toQueryBuilder(final T filter, final P props) {
        return toFieldQueryBuilder(filter, props)
                .field(filter.getField());
    }

    @SuppressWarnings("java:S1452")
    protected abstract FieldQueryBuilder<?, ?> toFieldQueryBuilder(T filter, final P props);
}
