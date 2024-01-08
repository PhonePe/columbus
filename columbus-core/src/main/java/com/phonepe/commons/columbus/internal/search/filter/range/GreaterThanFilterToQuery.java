package com.phonepe.commons.columbus.internal.search.filter.range;

import com.phonepe.commons.columbus.internal.search.filter.FieldFilterToQuery;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.filter.range.GreaterThanFilter;
import com.phonepe.commons.columbus.search.query.FieldQuery.FieldQueryBuilder;
import com.phonepe.commons.columbus.search.query.term.RangeQuery;
import com.phonepe.commons.columbus.search.query.visitor.QueryTransformationContext;

@RegistryKey(name = GreaterThanFilter.TYPE, type = GreaterThanFilter.class)
public class GreaterThanFilterToQuery<R, P, C extends QueryTransformationContext<R, P, C>>
        extends FieldFilterToQuery<R, GreaterThanFilter, P, C> {

    @Override
    protected FieldQueryBuilder<?, ?> toFieldQueryBuilder(final GreaterThanFilter filter, final P props) {
        return RangeQuery.builder()
                .from(filter.getValue())
                .includeLower(false);
    }
}
