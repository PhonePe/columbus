package com.phonepe.platform.columbus.internal.search.filter.range;

import com.phonepe.platform.columbus.internal.search.filter.FieldFilterToQuery;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.filter.range.GreaterThanFilter;
import com.phonepe.platform.columbus.search.query.FieldQuery.FieldQueryBuilder;
import com.phonepe.platform.columbus.search.query.term.RangeQuery;
import com.phonepe.platform.columbus.search.query.visitor.QueryTransformationContext;

@RegistryKey(name = GreaterThanFilter.TYPE, type = GreaterThanFilter.class)
public class GreaterThanFilterToQuery<R, C extends QueryTransformationContext<R, C>>
        extends FieldFilterToQuery<R, GreaterThanFilter, C> {

    @Override
    protected FieldQueryBuilder<?, ?> toFieldQueryBuilder(final GreaterThanFilter filter) {
        return RangeQuery.builder()
                .from(filter.getValue())
                .includeLower(false);
    }
}
