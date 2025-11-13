package com.phonepe.commons.columbus.elastic9.internal.search.query.simple;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchNoneQuery.Builder;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import com.phonepe.commons.columbus.elastic9.internal.search.query.AbstractQueryToElastic;
import com.phonepe.commons.columbus.elastic9.search.query.ElasticQueryTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.query.simple.MatchNoneQuery;

@RegistryKey(name = MatchNoneQuery.TYPE, type = MatchNoneQuery.class)
public class MatchNoneQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<MatchNoneQuery, C> {

    @Override
    public QueryVariant visit(final MatchNoneQuery query, final Object props, final C context) {
        final var builder = new Builder();
        applyCommonProperties(query, builder);
        return builder.build();
    }
}
