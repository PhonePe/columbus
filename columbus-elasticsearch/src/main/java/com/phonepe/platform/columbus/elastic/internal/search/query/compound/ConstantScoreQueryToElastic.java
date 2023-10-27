package com.phonepe.platform.columbus.elastic.internal.search.query.compound;

import com.phonepe.platform.columbus.elastic.internal.search.query.BaseQueryToElastic;
import com.phonepe.platform.columbus.elastic.search.query.ElasticQueryTransformationContext;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.query.compound.ConstantScoreQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.Objects;

@RegistryKey(name = ConstantScoreQuery.TYPE, type = ConstantScoreQuery.class)
public class ConstantScoreQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends BaseQueryToElastic<ConstantScoreQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final ConstantScoreQuery query, final C context) {
        final var innerQuery = Objects.requireNonNull(query.getQuery(), "query must not be null");
        return QueryBuilders.constantScoreQuery(context.transform(innerQuery));
    }
}
