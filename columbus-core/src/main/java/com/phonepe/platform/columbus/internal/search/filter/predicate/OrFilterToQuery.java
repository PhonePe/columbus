package com.phonepe.platform.columbus.internal.search.filter.predicate;

import com.phonepe.platform.columbus.internal.search.filter.FilterToQuery;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.filter.predicate.OrFilter;
import com.phonepe.platform.columbus.search.query.BaseQuery.BaseQueryBuilder;
import com.phonepe.platform.columbus.search.query.compound.BoolQuery;
import com.phonepe.platform.columbus.search.query.visitor.QueryTransformationContext;

@RegistryKey(name = OrFilter.TYPE, type = OrFilter.class)
public class OrFilterToQuery<R, P, C extends QueryTransformationContext<R, P, C>>
        extends FilterToQuery<R, OrFilter, P, C> {

    @Override
    protected BaseQueryBuilder<?, ?> toQueryBuilder(final OrFilter filter, final P props) {
        return BoolQuery.builder()
                .shouldClauses(filter.getFilters());
    }
}
