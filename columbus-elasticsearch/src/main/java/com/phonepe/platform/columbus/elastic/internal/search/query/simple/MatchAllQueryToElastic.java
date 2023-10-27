package com.phonepe.platform.columbus.elastic.internal.search.query.simple;

import com.phonepe.platform.columbus.elastic.internal.search.query.BaseQueryToElastic;
import com.phonepe.platform.columbus.elastic.search.query.ElasticQueryTransformationContext;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.query.simple.MatchAllQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

@RegistryKey(name = MatchAllQuery.TYPE, type = MatchAllQuery.class)
public class MatchAllQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends BaseQueryToElastic<MatchAllQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final MatchAllQuery query, final C context) {
        return QueryBuilders.matchAllQuery();
    }
}
