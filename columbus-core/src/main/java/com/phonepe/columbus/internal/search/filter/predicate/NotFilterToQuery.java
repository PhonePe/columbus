package com.phonepe.columbus.internal.search.filter.predicate;

import com.phonepe.columbus.internal.search.filter.FilterToQuery;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.filter.predicate.NotFilter;
import com.phonepe.columbus.search.query.AbstractQuery.AbstractQueryBuilder;
import com.phonepe.columbus.search.query.compound.BoolQuery;
import com.phonepe.columbus.search.query.visitor.QueryTransformationContext;

@RegistryKey(name = NotFilter.TYPE, type = NotFilter.class)
public class NotFilterToQuery<R, P, C extends QueryTransformationContext<R, P, C>>
        extends FilterToQuery<R, NotFilter, P, C> {

    @Override
    protected AbstractQueryBuilder<?, ?> toQueryBuilder(final NotFilter filter, final P props) {
        return BoolQuery.builder()
                .mustNot(filter.getFilter());
    }
}
