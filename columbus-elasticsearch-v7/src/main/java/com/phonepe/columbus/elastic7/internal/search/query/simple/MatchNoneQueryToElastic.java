package com.phonepe.columbus.elastic7.internal.search.query.simple;

import com.phonepe.columbus.elastic7.internal.search.query.AbstractQueryToElastic;
import com.phonepe.columbus.elastic7.search.query.ElasticQueryTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.query.simple.MatchNoneQuery;
import org.elasticsearch.index.query.MatchNoneQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

@RegistryKey(name = MatchNoneQuery.TYPE, type = MatchNoneQuery.class)
public class MatchNoneQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<MatchNoneQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final MatchNoneQuery query, final Object props, final C context) {
        return new MatchNoneQueryBuilder();
    }
}
