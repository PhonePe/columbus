package com.phonepe.columbus.elastic7.internal.search.query.term;

import com.phonepe.columbus.elastic7.internal.search.query.AbstractQueryToElastic;
import com.phonepe.columbus.elastic7.search.query.ElasticQueryTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.query.term.PrefixQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = PrefixQuery.TYPE, type = PrefixQuery.class)
public class PrefixQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<PrefixQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final PrefixQuery query, final Object props, final C context) {
        final var builder = QueryBuilders.prefixQuery(query.getField(), query.getValue());
        applyIfNotNull(query.getCaseInsensitive(), builder::caseInsensitive);
        applyIfNotNull(query.getRewrite(), builder::rewrite);
        return builder;
    }
}
