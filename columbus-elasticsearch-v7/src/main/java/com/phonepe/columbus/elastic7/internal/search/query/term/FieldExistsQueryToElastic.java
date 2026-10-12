package com.phonepe.columbus.elastic7.internal.search.query.term;

import com.phonepe.columbus.elastic7.internal.search.query.AbstractQueryToElastic;
import com.phonepe.columbus.elastic7.search.query.ElasticQueryTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.query.term.FieldExistsQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

@RegistryKey(name = FieldExistsQuery.TYPE, type = FieldExistsQuery.class)
public class FieldExistsQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<FieldExistsQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final FieldExistsQuery query, final Object props, final C context) {
        return QueryBuilders.existsQuery(query.getField());
    }
}
