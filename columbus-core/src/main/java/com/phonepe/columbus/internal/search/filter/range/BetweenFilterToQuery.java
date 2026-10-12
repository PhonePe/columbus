package com.phonepe.columbus.internal.search.filter.range;

import com.phonepe.columbus.internal.search.filter.FieldFilterToQuery;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.filter.range.BetweenFilter;
import com.phonepe.columbus.search.query.FieldQuery.FieldQueryBuilder;
import com.phonepe.columbus.search.query.term.RangeQuery;
import com.phonepe.columbus.search.query.visitor.QueryTransformationContext;

@RegistryKey(name = BetweenFilter.TYPE, type = BetweenFilter.class)
public class BetweenFilterToQuery<R, P, C extends QueryTransformationContext<R, P, C>>
        extends FieldFilterToQuery<R, BetweenFilter, P, C> {

    @Override
    protected FieldQueryBuilder<?, ?> toFieldQueryBuilder(final BetweenFilter filter, final P props) {
        return RangeQuery.builder()
                .from(filter.getFrom())
                .to(filter.getTo())
                .includeLower(filter.includeLower())
                .includeUpper(filter.includeUpper());
    }
}
