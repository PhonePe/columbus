package com.phonepe.commons.columbus.elastic.internal.search.query.simple;

import com.phonepe.commons.columbus.elastic.internal.search.query.AbstractQueryToElastic;
import com.phonepe.commons.columbus.elastic.search.query.ElasticQueryTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.query.simple.MatchAllQuery;
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
