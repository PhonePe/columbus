package com.phonepe.commons.columbus.internal.search.filter.predicate;

import com.phonepe.commons.columbus.internal.search.filter.FilterToQuery;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.filter.predicate.OrFilter;
import com.phonepe.commons.columbus.search.query.AbstractQuery.AbstractQueryBuilder;
import com.phonepe.commons.columbus.search.query.compound.BoolQuery;
import com.phonepe.commons.columbus.search.query.visitor.QueryTransformationContext;

@RegistryKey(name = OrFilter.TYPE, type = OrFilter.class)
public class OrFilterToQuery<R, P, C extends QueryTransformationContext<R, P, C>>
        extends FilterToQuery<R, OrFilter, P, C> {

    @Override
    protected AbstractQueryBuilder<?, ?> toQueryBuilder(final OrFilter filter, final P props) {
        return BoolQuery.builder()
                .shouldClauses(filter.getFilters());
    }
}
