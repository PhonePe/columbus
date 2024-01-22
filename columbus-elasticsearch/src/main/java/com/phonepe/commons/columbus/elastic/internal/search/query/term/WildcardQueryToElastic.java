package com.phonepe.commons.columbus.elastic.internal.search.query.term;

import com.phonepe.commons.columbus.elastic.internal.search.query.AbstractQueryToElastic;
import com.phonepe.commons.columbus.elastic.search.query.ElasticQueryTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.query.term.WildcardQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = WildcardQuery.TYPE, type = WildcardQuery.class)
public class WildcardQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<WildcardQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final WildcardQuery query, final Object props, final C context) {
        final var builder = QueryBuilders.wildcardQuery(query.getField(), query.getValue());
        applyIfNotNull(query.getCaseInsensitive(), builder::caseInsensitive);
        applyIfNotNull(query.getRewrite(), builder::rewrite);
        return builder;
    }
}
