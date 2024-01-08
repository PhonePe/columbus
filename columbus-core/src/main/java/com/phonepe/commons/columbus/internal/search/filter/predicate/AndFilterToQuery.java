package com.phonepe.commons.columbus.internal.search.filter.predicate;

import com.phonepe.commons.columbus.internal.search.filter.FilterToQuery;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.filter.predicate.AndFilter;
import com.phonepe.commons.columbus.search.query.BaseQuery.BaseQueryBuilder;
import com.phonepe.commons.columbus.search.query.compound.BoolQuery;
import com.phonepe.commons.columbus.search.query.visitor.QueryTransformationContext;

@RegistryKey(name = AndFilter.TYPE, type = AndFilter.class)
public class AndFilterToQuery<R, P, C extends QueryTransformationContext<R, P, C>>
        extends FilterToQuery<R, AndFilter, P, C> {

    @Override
    protected BaseQueryBuilder<?, ?> toQueryBuilder(final AndFilter filter, final P props) {
        return BoolQuery.builder()
                .mustClauses(filter.getFilters());
    }
}
