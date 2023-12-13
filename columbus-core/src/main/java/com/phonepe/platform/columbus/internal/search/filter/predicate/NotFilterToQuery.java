package com.phonepe.platform.columbus.internal.search.filter.predicate;

import com.phonepe.platform.columbus.internal.search.filter.FilterToQuery;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.filter.predicate.NotFilter;
import com.phonepe.platform.columbus.search.query.BaseQuery.BaseQueryBuilder;
import com.phonepe.platform.columbus.search.query.compound.BoolQuery;
import com.phonepe.platform.columbus.search.query.visitor.QueryTransformationContext;

@RegistryKey(name = NotFilter.TYPE, type = NotFilter.class)
public class NotFilterToQuery<R, P, C extends QueryTransformationContext<R, P, C>>
        extends FilterToQuery<R, NotFilter, P, C> {

    @Override
    protected BaseQueryBuilder<?, ?> toQueryBuilder(final NotFilter filter, final P props) {
        return BoolQuery.builder()
                .mustNot(filter.getFilter());
    }
}
