package com.phonepe.platform.columbus.elastic.internal.search.query.term;

import com.phonepe.platform.columbus.elastic.internal.search.query.BaseQueryToElastic;
import com.phonepe.platform.columbus.elastic.search.query.ElasticQueryTransformationContext;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.query.term.WildcardQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import static com.phonepe.platform.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = WildcardQuery.TYPE, type = WildcardQuery.class)
public class WildcardQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends BaseQueryToElastic<WildcardQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final WildcardQuery query, final Object props, final C context) {
        final var builder = QueryBuilders.wildcardQuery(query.getField(), query.getValue());
        applyIfNotNull(query.getCaseInsensitive(), builder::caseInsensitive);
        applyIfNotNull(query.getRewrite(), builder::rewrite);
        return builder;
    }
}
