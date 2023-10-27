package com.phonepe.platform.columbus.internal.search.filter.general;

import com.phonepe.platform.columbus.internal.search.filter.FieldFilterToQuery;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.filter.general.EqualsFilter;
import com.phonepe.platform.columbus.search.query.FieldQuery.FieldQueryBuilder;
import com.phonepe.platform.columbus.search.query.term.TermQuery;
import com.phonepe.platform.columbus.search.query.visitor.QueryTransformationContext;

@RegistryKey(name = EqualsFilter.TYPE, type = EqualsFilter.class)
public class EqualsFilterToQuery<R, C extends QueryTransformationContext<R, C>>
        extends FieldFilterToQuery<R, EqualsFilter, C> {

    @Override
    protected FieldQueryBuilder<?, ?> toFieldQueryBuilder(final EqualsFilter filter) {
        return TermQuery.builder()
                .value(filter.getValue());
    }
}
