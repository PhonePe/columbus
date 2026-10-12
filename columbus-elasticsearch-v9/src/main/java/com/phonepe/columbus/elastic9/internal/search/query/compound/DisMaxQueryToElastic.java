package com.phonepe.columbus.elastic9.internal.search.query.compound;

import co.elastic.clients.elasticsearch._types.query_dsl.DisMaxQuery.Builder;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import com.phonepe.columbus.elastic9.internal.search.query.AbstractQueryToElastic;
import com.phonepe.columbus.elastic9.search.query.ElasticQueryTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.query.compound.DisMaxQuery;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = DisMaxQuery.TYPE, type = DisMaxQuery.class)
public class DisMaxQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<DisMaxQuery, C> {

    @Override
    public QueryVariant visit(final DisMaxQuery query, final Object props, final C context) {
        final var builder = new Builder();
        applyCommonProperties(query, builder);
        query.getQueries().stream()
                .map(innerQuery -> context.transform(innerQuery, props))
                .forEachOrdered(builder::queries);
        applyIfNotNull(query.getTieBreaker(), Float::doubleValue, builder::tieBreaker);
        return builder.build();
    }
}
