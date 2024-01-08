package com.phonepe.commons.columbus.elastic.internal.search.query.term;

import com.phonepe.commons.columbus.elastic.internal.search.query.BaseQueryToElastic;
import com.phonepe.commons.columbus.elastic.search.query.ElasticQueryTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.query.term.TermQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = TermQuery.TYPE, type = TermQuery.class)
public class TermQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends BaseQueryToElastic<TermQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final TermQuery query, final Object props, final C context) {
        final var builder = QueryBuilders.termQuery(query.getField(), query.getValue());
        applyIfNotNull(query.getCaseInsensitive(), builder::caseInsensitive);
        return builder;
    }
}
