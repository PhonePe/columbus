package com.phonepe.columbus.internal.search.filter.score;

import com.phonepe.columbus.internal.search.filter.FilterToQuery;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.filter.score.FixedScoreFilter;
import com.phonepe.columbus.search.query.AbstractQuery.AbstractQueryBuilder;
import com.phonepe.columbus.search.query.compound.ConstantScoreQuery;
import com.phonepe.columbus.search.query.visitor.QueryTransformationContext;

@RegistryKey(name = FixedScoreFilter.TYPE, type = FixedScoreFilter.class)
public class FixedScoreFilterToQuery<R, P, C extends QueryTransformationContext<R, P, C>>
        extends FilterToQuery<R, FixedScoreFilter, P, C> {

    @Override
    protected AbstractQueryBuilder<?, ?> toQueryBuilder(final FixedScoreFilter filter, final P props) {
        return ConstantScoreQuery.builder()
                .query(filter.getFilter())
                .boost(filter.getBoost());
    }
}
