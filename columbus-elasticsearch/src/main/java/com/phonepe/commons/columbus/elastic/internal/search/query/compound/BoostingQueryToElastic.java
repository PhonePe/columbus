package com.phonepe.commons.columbus.elastic.internal.search.query.compound;

import com.phonepe.commons.columbus.elastic.internal.search.query.AbstractQueryToElastic;
import com.phonepe.commons.columbus.elastic.search.query.ElasticQueryTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.query.compound.BoostingQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.Objects;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = BoostingQuery.TYPE, type = BoostingQuery.class)
public class BoostingQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<BoostingQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final BoostingQuery query, final Object props, final C context) {
        final var positive = Objects.requireNonNull(query.getPositive(), "positive must not be null");
        final var negative = Objects.requireNonNull(query.getNegative(), "negative must not be null");
        final var builder = QueryBuilders.boostingQuery(
                context.transform(positive, props),
                context.transform(negative, props));
        applyIfNotNull(query.getNegativeBoost(), builder::negativeBoost);
        return builder;
    }
}
