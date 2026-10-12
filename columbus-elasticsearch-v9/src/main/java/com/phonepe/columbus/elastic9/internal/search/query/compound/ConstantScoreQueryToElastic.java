package com.phonepe.columbus.elastic9.internal.search.query.compound;

import co.elastic.clients.elasticsearch._types.query_dsl.ConstantScoreQuery.Builder;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import com.phonepe.columbus.elastic9.internal.search.query.AbstractQueryToElastic;
import com.phonepe.columbus.elastic9.search.query.ElasticQueryTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.query.compound.ConstantScoreQuery;

import java.util.Objects;


@RegistryKey(name = ConstantScoreQuery.TYPE, type = ConstantScoreQuery.class)
public class ConstantScoreQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<ConstantScoreQuery, C> {

    @Override
    public QueryVariant visit(final ConstantScoreQuery query, final Object props, final C context) {
        final var innerQuery = Objects.requireNonNull(query.getQuery(), "query must not be null");
        final var builder = new Builder()
                .filter(context.transform(innerQuery, props));
        applyCommonProperties(query, builder);
        return builder.build();
    }
}
