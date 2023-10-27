package com.phonepe.platform.columbus.internal.search.filter.predicate;

import com.phonepe.platform.columbus.internal.search.filter.FilterToQuery;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.filter.predicate.OrFilter;
import com.phonepe.platform.columbus.search.query.BaseQuery.BaseQueryBuilder;
import com.phonepe.platform.columbus.search.query.compound.BoolQuery;
import com.phonepe.platform.columbus.search.query.visitor.QueryTransformationContext;

@RegistryKey(name = OrFilter.TYPE, type = OrFilter.class)
public class OrFilterToQuery<R, C extends QueryTransformationContext<R, C>> extends FilterToQuery<R, OrFilter, C> {

    @Override
    protected BaseQueryBuilder<?, ?> toQueryBuilder(final OrFilter filter) {
        return BoolQuery.builder()
                .shouldClauses(filter.getFilters());
    }
}
