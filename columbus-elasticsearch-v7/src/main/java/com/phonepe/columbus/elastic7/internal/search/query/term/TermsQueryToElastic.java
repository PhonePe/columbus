package com.phonepe.columbus.elastic7.internal.search.query.term;

import com.phonepe.columbus.elastic7.internal.search.query.AbstractQueryToElastic;
import com.phonepe.columbus.elastic7.search.query.ElasticQueryTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.query.term.TermsQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

@RegistryKey(name = TermsQuery.TYPE, type = TermsQuery.class)
public class TermsQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<TermsQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final TermsQuery query, final Object props, final C context) {
        return QueryBuilders.termsQuery(query.getField(), query.getValues());
    }
}
