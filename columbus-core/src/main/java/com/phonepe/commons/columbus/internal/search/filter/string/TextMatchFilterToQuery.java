package com.phonepe.commons.columbus.internal.search.filter.string;

import com.phonepe.commons.columbus.internal.search.filter.FieldFilterToQuery;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.filter.string.TextMatchFilter;
import com.phonepe.commons.columbus.search.query.FieldQuery.FieldQueryBuilder;
import com.phonepe.commons.columbus.search.query.text.MatchQuery;
import com.phonepe.commons.columbus.search.query.visitor.QueryTransformationContext;

@RegistryKey(name = TextMatchFilter.TYPE, type = TextMatchFilter.class)
public class TextMatchFilterToQuery<R, P, C extends QueryTransformationContext<R, P, C>>
        extends FieldFilterToQuery<R, TextMatchFilter, P, C> {

    @Override
    protected FieldQueryBuilder<?, ?> toFieldQueryBuilder(final TextMatchFilter filter, final P props) {
        return MatchQuery.builder()
                .value(filter.getValue());
    }
}
