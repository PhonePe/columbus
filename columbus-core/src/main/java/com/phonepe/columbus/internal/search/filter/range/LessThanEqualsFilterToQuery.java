package com.phonepe.columbus.internal.search.filter.range;

import com.phonepe.columbus.internal.search.filter.FieldFilterToQuery;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.filter.range.LessThanEqualsFilter;
import com.phonepe.columbus.search.query.FieldQuery.FieldQueryBuilder;
import com.phonepe.columbus.search.query.term.RangeQuery;
import com.phonepe.columbus.search.query.visitor.QueryTransformationContext;

@RegistryKey(name = LessThanEqualsFilter.TYPE, type = LessThanEqualsFilter.class)
public class LessThanEqualsFilterToQuery<R, P, C extends QueryTransformationContext<R, P, C>>
        extends FieldFilterToQuery<R, LessThanEqualsFilter, P, C> {

    @Override
    protected FieldQueryBuilder<?, ?> toFieldQueryBuilder(final LessThanEqualsFilter filter, final P props) {
        return RangeQuery.builder()
                .to(filter.getValue());
    }
}
