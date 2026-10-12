package com.phonepe.columbus.internal.search.filter.predicate;

import com.phonepe.columbus.internal.search.filter.FilterToQuery;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.filter.predicate.AndFilter;
import com.phonepe.columbus.search.query.AbstractQuery.AbstractQueryBuilder;
import com.phonepe.columbus.search.query.compound.BoolQuery;
import com.phonepe.columbus.search.query.visitor.QueryTransformationContext;

@RegistryKey(name = AndFilter.TYPE, type = AndFilter.class)
public class AndFilterToQuery<R, P, C extends QueryTransformationContext<R, P, C>>
        extends FilterToQuery<R, AndFilter, P, C> {

    @Override
    protected AbstractQueryBuilder<?, ?> toQueryBuilder(final AndFilter filter, final P props) {
        return BoolQuery.builder()
                .mustClauses(filter.getFilters());
    }
}
