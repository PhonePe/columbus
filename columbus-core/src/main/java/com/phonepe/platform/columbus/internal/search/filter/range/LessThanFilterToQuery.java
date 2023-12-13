package com.phonepe.platform.columbus.internal.search.filter.range;

import com.phonepe.platform.columbus.internal.search.filter.FieldFilterToQuery;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.filter.range.LessThanFilter;
import com.phonepe.platform.columbus.search.query.FieldQuery.FieldQueryBuilder;
import com.phonepe.platform.columbus.search.query.term.RangeQuery;
import com.phonepe.platform.columbus.search.query.visitor.QueryTransformationContext;

@RegistryKey(name = LessThanFilter.TYPE, type = LessThanFilter.class)
public class LessThanFilterToQuery<R, P, C extends QueryTransformationContext<R, P, C>>
        extends FieldFilterToQuery<R, LessThanFilter, P, C> {

    @Override
    protected FieldQueryBuilder<?, ?> toFieldQueryBuilder(final LessThanFilter filter, final P props) {
        return RangeQuery.builder()
                .to(filter.getValue())
                .includeUpper(false);
    }
}
