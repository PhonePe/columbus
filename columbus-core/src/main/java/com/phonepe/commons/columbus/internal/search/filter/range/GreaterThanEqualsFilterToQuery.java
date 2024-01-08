package com.phonepe.commons.columbus.internal.search.filter.range;

import com.phonepe.commons.columbus.internal.search.filter.FieldFilterToQuery;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.filter.range.GreaterThanEqualsFilter;
import com.phonepe.commons.columbus.search.query.FieldQuery.FieldQueryBuilder;
import com.phonepe.commons.columbus.search.query.term.RangeQuery;
import com.phonepe.commons.columbus.search.query.visitor.QueryTransformationContext;

@RegistryKey(name = GreaterThanEqualsFilter.TYPE, type = GreaterThanEqualsFilter.class)
public class GreaterThanEqualsFilterToQuery<R, P, C extends QueryTransformationContext<R, P, C>>
        extends FieldFilterToQuery<R, GreaterThanEqualsFilter, P, C> {

    @Override
    protected FieldQueryBuilder<?, ?> toFieldQueryBuilder(final GreaterThanEqualsFilter filter, final P props) {
        return RangeQuery.builder()
                .from(filter.getValue());
    }
}
