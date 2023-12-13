package com.phonepe.platform.columbus.internal.search.filter.range;

import com.phonepe.platform.columbus.internal.search.filter.FieldFilterToQuery;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.filter.range.GreaterThanEqualsFilter;
import com.phonepe.platform.columbus.search.query.FieldQuery.FieldQueryBuilder;
import com.phonepe.platform.columbus.search.query.term.RangeQuery;
import com.phonepe.platform.columbus.search.query.visitor.QueryTransformationContext;

@RegistryKey(name = GreaterThanEqualsFilter.TYPE, type = GreaterThanEqualsFilter.class)
public class GreaterThanEqualsFilterToQuery<R, P, C extends QueryTransformationContext<R, P, C>>
        extends FieldFilterToQuery<R, GreaterThanEqualsFilter, P, C> {

    @Override
    protected FieldQueryBuilder<?, ?> toFieldQueryBuilder(final GreaterThanEqualsFilter filter, final P props) {
        return RangeQuery.builder()
                .from(filter.getValue());
    }
}
