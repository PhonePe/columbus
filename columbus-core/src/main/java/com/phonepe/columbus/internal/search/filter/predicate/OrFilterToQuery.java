package com.phonepe.columbus.internal.search.filter.predicate;

import com.phonepe.columbus.internal.search.filter.FilterToQuery;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.filter.predicate.OrFilter;
import com.phonepe.columbus.search.query.AbstractQuery.AbstractQueryBuilder;
import com.phonepe.columbus.search.query.compound.BoolQuery;
import com.phonepe.columbus.search.query.visitor.QueryTransformationContext;

@RegistryKey(name = OrFilter.TYPE, type = OrFilter.class)
public class OrFilterToQuery<R, P, C extends QueryTransformationContext<R, P, C>>
        extends FilterToQuery<R, OrFilter, P, C> {

    @Override
    protected AbstractQueryBuilder<?, ?> toQueryBuilder(final OrFilter filter, final P props) {
        return BoolQuery.builder()
                .shouldClauses(filter.getFilters());
    }
}
