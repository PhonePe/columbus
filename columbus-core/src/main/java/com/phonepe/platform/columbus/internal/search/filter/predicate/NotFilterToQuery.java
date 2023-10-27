package com.phonepe.platform.columbus.internal.search.filter.predicate;

import com.phonepe.platform.columbus.internal.search.filter.FilterToQuery;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.filter.predicate.NotFilter;
import com.phonepe.platform.columbus.search.query.BaseQuery.BaseQueryBuilder;
import com.phonepe.platform.columbus.search.query.compound.BoolQuery;
import com.phonepe.platform.columbus.search.query.visitor.QueryTransformationContext;

@RegistryKey(name = NotFilter.TYPE, type = NotFilter.class)
public class NotFilterToQuery<R, C extends QueryTransformationContext<R, C>> extends FilterToQuery<R, NotFilter, C> {

    @Override
    protected BaseQueryBuilder<?, ?> toQueryBuilder(final NotFilter filter) {
        return BoolQuery.builder()
                .mustNot(filter.getFilter());
    }
}
