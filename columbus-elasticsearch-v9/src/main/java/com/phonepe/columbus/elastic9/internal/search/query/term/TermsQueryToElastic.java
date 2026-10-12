package com.phonepe.columbus.elastic9.internal.search.query.term;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQuery.Builder;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQueryField;
import com.phonepe.columbus.elastic9.internal.search.query.AbstractQueryToElastic;
import com.phonepe.columbus.elastic9.search.query.ElasticQueryTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.query.term.TermsQuery;

import java.util.List;


@RegistryKey(name = TermsQuery.TYPE, type = TermsQuery.class)
public class TermsQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<TermsQuery, C> {

    @Override
    public QueryVariant visit(final TermsQuery query, final Object props, final C context) {
        final List<FieldValue> fieldValues = query.getValues().stream()
                .map(FieldValue::of)
                .toList();

        final var builder = new Builder()
                .field(query.getField())
                .terms(new TermsQueryField.Builder()
                        .value(fieldValues)
                        .build());
        applyCommonProperties(query, builder);
        return builder.build();
    }
}
