package com.phonepe.platform.columbus.internal.search.filter;

import com.phonepe.platform.columbus.search.filter.FieldFilter;
import com.phonepe.platform.columbus.search.query.BaseQuery.BaseQueryBuilder;
import com.phonepe.platform.columbus.search.query.FieldQuery.FieldQueryBuilder;
import com.phonepe.platform.columbus.search.query.visitor.QueryTransformationContext;

public abstract class FieldFilterToQuery<R, T extends FieldFilter, C extends QueryTransformationContext<R, C>>
        extends FilterToQuery<R, T, C> {

    @Override
    protected BaseQueryBuilder<?, ?> toQueryBuilder(final T filter) {
        return toFieldQueryBuilder(filter)
                .field(filter.getField());
    }

    @SuppressWarnings("java:S1452")
    protected abstract FieldQueryBuilder<?, ?> toFieldQueryBuilder(T filter);
}
