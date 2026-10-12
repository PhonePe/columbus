package com.phonepe.columbus.internal.search.filter.general;

import com.phonepe.columbus.internal.search.filter.FieldFilterToQuery;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.filter.general.InFilter;
import com.phonepe.columbus.search.query.FieldQuery.FieldQueryBuilder;
import com.phonepe.columbus.search.query.term.TermsQuery;
import com.phonepe.columbus.search.query.visitor.QueryTransformationContext;

@RegistryKey(name = InFilter.TYPE, type = InFilter.class)
public class InFilterToQuery<R, P, C extends QueryTransformationContext<R, P, C>>
        extends FieldFilterToQuery<R, InFilter, P, C> {

    @Override
    protected FieldQueryBuilder<?, ?> toFieldQueryBuilder(final InFilter filter, final P props) {
        return TermsQuery.builder()
                .values(filter.getValues());
    }
}
