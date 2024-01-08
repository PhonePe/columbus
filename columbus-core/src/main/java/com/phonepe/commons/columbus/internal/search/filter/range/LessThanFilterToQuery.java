package com.phonepe.commons.columbus.internal.search.filter.range;

import com.phonepe.commons.columbus.internal.search.filter.FieldFilterToQuery;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.filter.range.LessThanFilter;
import com.phonepe.commons.columbus.search.query.FieldQuery.FieldQueryBuilder;
import com.phonepe.commons.columbus.search.query.term.RangeQuery;
import com.phonepe.commons.columbus.search.query.visitor.QueryTransformationContext;

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
