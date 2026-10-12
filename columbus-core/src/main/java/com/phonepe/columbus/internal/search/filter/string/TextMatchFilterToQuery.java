package com.phonepe.columbus.internal.search.filter.string;

import com.phonepe.columbus.internal.search.filter.FieldFilterToQuery;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.filter.string.TextMatchFilter;
import com.phonepe.columbus.search.query.FieldQuery.FieldQueryBuilder;
import com.phonepe.columbus.search.query.text.MatchQuery;
import com.phonepe.columbus.search.query.visitor.QueryTransformationContext;

@RegistryKey(name = TextMatchFilter.TYPE, type = TextMatchFilter.class)
public class TextMatchFilterToQuery<R, P, C extends QueryTransformationContext<R, P, C>>
        extends FieldFilterToQuery<R, TextMatchFilter, P, C> {

    @Override
    protected FieldQueryBuilder<?, ?> toFieldQueryBuilder(final TextMatchFilter filter, final P props) {
        return MatchQuery.builder()
                .value(filter.getValue());
    }
}
