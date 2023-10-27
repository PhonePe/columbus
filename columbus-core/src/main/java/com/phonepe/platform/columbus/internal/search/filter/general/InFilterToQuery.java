package com.phonepe.platform.columbus.internal.search.filter.general;

import com.phonepe.platform.columbus.internal.search.filter.FieldFilterToQuery;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.filter.general.InFilter;
import com.phonepe.platform.columbus.search.query.FieldQuery.FieldQueryBuilder;
import com.phonepe.platform.columbus.search.query.term.TermsQuery;
import com.phonepe.platform.columbus.search.query.visitor.QueryTransformationContext;

@RegistryKey(name = InFilter.TYPE, type = InFilter.class)
public class InFilterToQuery<R, C extends QueryTransformationContext<R, C>> extends FieldFilterToQuery<R, InFilter, C> {

    @Override
    protected FieldQueryBuilder<?, ?> toFieldQueryBuilder(final InFilter filter) {
        return TermsQuery.builder()
                .values(filter.getValues());
    }
}
