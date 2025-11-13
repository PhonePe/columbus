package com.phonepe.commons.columbus.elastic9.internal.search.query.compound;

import co.elastic.clients.elasticsearch._types.query_dsl.BoostingQuery.Builder;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import com.phonepe.commons.columbus.elastic9.internal.search.query.AbstractQueryToElastic;
import com.phonepe.commons.columbus.elastic9.search.query.ElasticQueryTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.query.compound.BoostingQuery;

import java.util.Objects;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = BoostingQuery.TYPE, type = BoostingQuery.class)
public class BoostingQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<BoostingQuery, C> {

    @Override
    public QueryVariant visit(final BoostingQuery query, final Object props, final C context) {
        final var positive = Objects.requireNonNull(query.getPositive(), "positive must not be null");
        final var negative = Objects.requireNonNull(query.getNegative(), "negative must not be null");
        final var builder = new Builder()
                .positive(context.transform(positive, props))
                .negative(context.transform(negative, props));
        applyCommonProperties(query, builder);
        applyIfNotNull((double) query.getNegativeBoost(), builder::negativeBoost);
        return builder.build();
    }
}
