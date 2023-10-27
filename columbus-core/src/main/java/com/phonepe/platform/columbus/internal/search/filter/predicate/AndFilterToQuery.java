package com.phonepe.platform.columbus.internal.search.filter.predicate;

import com.phonepe.platform.columbus.internal.search.filter.FilterToQuery;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.filter.predicate.AndFilter;
import com.phonepe.platform.columbus.search.query.BaseQuery.BaseQueryBuilder;
import com.phonepe.platform.columbus.search.query.compound.BoolQuery;
import com.phonepe.platform.columbus.search.query.visitor.QueryTransformationContext;

@RegistryKey(name = AndFilter.TYPE, type = AndFilter.class)
public class AndFilterToQuery<R, C extends QueryTransformationContext<R, C>> extends FilterToQuery<R, AndFilter, C> {

    @Override
    protected BaseQueryBuilder<?, ?> toQueryBuilder(final AndFilter filter) {
        return BoolQuery.builder()
                .filterClauses(filter.getFilters());
    }
}
