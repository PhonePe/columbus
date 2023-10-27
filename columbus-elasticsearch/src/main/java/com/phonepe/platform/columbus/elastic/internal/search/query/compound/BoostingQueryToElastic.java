package com.phonepe.platform.columbus.elastic.internal.search.query.compound;

import com.phonepe.platform.columbus.elastic.internal.search.query.BaseQueryToElastic;
import com.phonepe.platform.columbus.elastic.search.query.ElasticQueryTransformationContext;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.query.compound.BoostingQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.Objects;

import static com.phonepe.platform.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = BoostingQuery.TYPE, type = BoostingQuery.class)
public class BoostingQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends BaseQueryToElastic<BoostingQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final BoostingQuery query, final C context) {
        final var positive = Objects.requireNonNull(query.getPositive(), "positive must not be null");
        final var negative = Objects.requireNonNull(query.getNegative(), "negative must not be null");
        final var builder = QueryBuilders.boostingQuery(
                context.transform(positive),
                context.transform(negative));
        applyIfNotNull(query.getNegativeBoost(), builder::negativeBoost);
        return builder;
    }
}
