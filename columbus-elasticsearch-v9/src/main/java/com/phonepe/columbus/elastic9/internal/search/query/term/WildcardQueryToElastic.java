package com.phonepe.columbus.elastic9.internal.search.query.term;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import co.elastic.clients.elasticsearch._types.query_dsl.WildcardQuery.Builder;
import com.phonepe.columbus.elastic9.internal.search.query.AbstractQueryToElastic;
import com.phonepe.columbus.elastic9.search.query.ElasticQueryTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.query.term.WildcardQuery;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = WildcardQuery.TYPE, type = WildcardQuery.class)
public class WildcardQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<WildcardQuery, C> {

    @Override
    public QueryVariant visit(final WildcardQuery query, final Object props, final C context) {
        final var builder = new Builder()
                .field(query.getField())
                .value(query.getValue());
        applyCommonProperties(query, builder);
        applyIfNotNull(query.getCaseInsensitive(), builder::caseInsensitive);
        applyIfNotNull(query.getRewrite(), builder::rewrite);
        return builder.build();
    }
}
