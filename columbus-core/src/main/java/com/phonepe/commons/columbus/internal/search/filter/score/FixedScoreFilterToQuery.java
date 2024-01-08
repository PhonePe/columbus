package com.phonepe.commons.columbus.internal.search.filter.score;

import com.phonepe.commons.columbus.internal.search.filter.FilterToQuery;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.filter.score.FixedScoreFilter;
import com.phonepe.commons.columbus.search.query.BaseQuery.BaseQueryBuilder;
import com.phonepe.commons.columbus.search.query.compound.ConstantScoreQuery;
import com.phonepe.commons.columbus.search.query.visitor.QueryTransformationContext;

@RegistryKey(name = FixedScoreFilter.TYPE, type = FixedScoreFilter.class)
public class FixedScoreFilterToQuery<R, P, C extends QueryTransformationContext<R, P, C>>
        extends FilterToQuery<R, FixedScoreFilter, P, C> {

    @Override
    protected BaseQueryBuilder<?, ?> toQueryBuilder(final FixedScoreFilter filter, final P props) {
        return ConstantScoreQuery.builder()
                .query(filter.getFilter())
                .boost(filter.getBoost());
    }
}
