package com.phonepe.platform.columbus.elastic.internal.search.query.term;

import com.phonepe.platform.columbus.elastic.internal.search.query.BaseQueryToElastic;
import com.phonepe.platform.columbus.elastic.search.query.ElasticQueryTransformationContext;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.query.term.TermsQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

@RegistryKey(name = TermsQuery.TYPE, type = TermsQuery.class)
public class TermsQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends BaseQueryToElastic<TermsQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final TermsQuery query, final Object props, final C context) {
        return QueryBuilders.termsQuery(query.getField(), query.getValues());
    }
}
