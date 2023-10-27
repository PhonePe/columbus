package com.phonepe.platform.columbus.elastic.internal.search.query.term;

import com.phonepe.platform.columbus.elastic.internal.search.query.BaseQueryToElastic;
import com.phonepe.platform.columbus.elastic.search.query.ElasticQueryTransformationContext;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.query.term.FieldExistsQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

@RegistryKey(name = FieldExistsQuery.TYPE, type = FieldExistsQuery.class)
public class FieldExistsQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends BaseQueryToElastic<FieldExistsQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final FieldExistsQuery query, final C context) {
        return QueryBuilders.existsQuery(query.getField());
    }
}
