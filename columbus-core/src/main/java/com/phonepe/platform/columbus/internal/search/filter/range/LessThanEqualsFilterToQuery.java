package com.phonepe.platform.columbus.internal.search.filter.range;

import com.phonepe.platform.columbus.internal.search.filter.FieldFilterToQuery;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.filter.range.LessThanEqualsFilter;
import com.phonepe.platform.columbus.search.query.FieldQuery.FieldQueryBuilder;
import com.phonepe.platform.columbus.search.query.term.RangeQuery;
import com.phonepe.platform.columbus.search.query.visitor.QueryTransformationContext;

@RegistryKey(name = LessThanEqualsFilter.TYPE, type = LessThanEqualsFilter.class)
public class LessThanEqualsFilterToQuery<R, C extends QueryTransformationContext<R, C>>
        extends FieldFilterToQuery<R, LessThanEqualsFilter, C> {

    @Override
    protected FieldQueryBuilder<?, ?> toFieldQueryBuilder(final LessThanEqualsFilter filter) {
        return RangeQuery.builder()
                .to(filter.getValue());
    }
}
