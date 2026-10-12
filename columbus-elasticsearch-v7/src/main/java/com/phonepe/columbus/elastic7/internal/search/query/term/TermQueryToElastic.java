package com.phonepe.columbus.elastic7.internal.search.query.term;

import com.phonepe.columbus.elastic7.internal.search.query.AbstractQueryToElastic;
import com.phonepe.columbus.elastic7.search.query.ElasticQueryTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.query.term.TermQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = TermQuery.TYPE, type = TermQuery.class)
public class TermQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<TermQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final TermQuery query, final Object props, final C context) {
        final var builder = QueryBuilders.termQuery(query.getField(), query.getValue());
        applyIfNotNull(query.getCaseInsensitive(), builder::caseInsensitive);
        return builder;
    }
}
