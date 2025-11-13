package com.phonepe.commons.columbus.elastic9.internal.search.query.simple;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery.Builder;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import com.phonepe.commons.columbus.elastic9.internal.search.query.AbstractQueryToElastic;
import com.phonepe.commons.columbus.elastic9.search.query.ElasticQueryTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.query.simple.MatchAllQuery;

@RegistryKey(name = MatchAllQuery.TYPE, type = MatchAllQuery.class)
public class MatchAllQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<MatchAllQuery, C> {

    @Override
    public QueryVariant visit(final MatchAllQuery query, final Object props, final C context) {
        final var builder = new Builder();
        applyCommonProperties(query, builder);
        return builder.build();
    }
}
