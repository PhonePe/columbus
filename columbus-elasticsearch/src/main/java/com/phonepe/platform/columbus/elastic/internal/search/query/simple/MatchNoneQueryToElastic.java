package com.phonepe.platform.columbus.elastic.internal.search.query.simple;

import com.phonepe.platform.columbus.elastic.internal.search.query.BaseQueryToElastic;
import com.phonepe.platform.columbus.elastic.search.query.ElasticQueryTransformationContext;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.query.simple.MatchNoneQuery;
import org.elasticsearch.index.query.MatchNoneQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

@RegistryKey(name = MatchNoneQuery.TYPE, type = MatchNoneQuery.class)
public class MatchNoneQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends BaseQueryToElastic<MatchNoneQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final MatchNoneQuery query, final Object props, final C context) {
        return new MatchNoneQueryBuilder();
    }
}
