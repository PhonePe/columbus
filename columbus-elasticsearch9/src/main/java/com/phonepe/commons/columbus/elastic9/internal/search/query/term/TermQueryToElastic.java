package com.phonepe.commons.columbus.elastic9.internal.search.query.term;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery.Builder;
import com.phonepe.commons.columbus.elastic9.internal.search.query.AbstractQueryToElastic;
import com.phonepe.commons.columbus.elastic9.search.query.ElasticQueryTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.query.term.TermQuery;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = TermQuery.TYPE, type = TermQuery.class)
public class TermQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<TermQuery, C> {

    @Override
    public QueryVariant visit(final TermQuery query, final Object props, final C context) {
        final var builder = new Builder()
                .field(query.getField())
                .value(FieldValue.of(query.getValue()));
        applyCommonProperties(query, builder);
        applyIfNotNull(query.getCaseInsensitive(), builder::caseInsensitive);
        return builder.build();
    }
}
