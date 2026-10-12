package com.phonepe.columbus.elastic7.internal.search.query.simple;

import com.phonepe.columbus.elastic7.internal.search.query.AbstractQueryToElastic;
import com.phonepe.columbus.elastic7.search.query.ElasticQueryTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.query.simple.MatchAllQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

@RegistryKey(name = MatchAllQuery.TYPE, type = MatchAllQuery.class)
public class MatchAllQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<MatchAllQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final MatchAllQuery query, final Object props, final C context) {
        return QueryBuilders.matchAllQuery();
    }
}
