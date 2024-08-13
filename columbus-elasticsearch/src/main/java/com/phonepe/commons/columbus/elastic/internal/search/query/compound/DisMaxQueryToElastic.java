package com.phonepe.commons.columbus.elastic.internal.search.query.compound;

import com.phonepe.commons.columbus.elastic.internal.search.query.AbstractQueryToElastic;
import com.phonepe.commons.columbus.elastic.search.query.ElasticQueryTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.query.compound.DisMaxQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = DisMaxQuery.TYPE, type = DisMaxQuery.class)
public class DisMaxQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<DisMaxQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final DisMaxQuery query, final Object props, final C context) {
        final var builder = QueryBuilders.disMaxQuery();

        query.getQueries().stream()
                .map(innerQuery -> context.transform(innerQuery, props))
                .forEachOrdered(builder::add);
        applyIfNotNull(query.getTieBreaker(), builder::tieBreaker);

        return builder;
    }
}
