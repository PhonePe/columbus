package com.phonepe.commons.columbus.elastic.internal.search.query.term;

import com.phonepe.commons.columbus.elastic.internal.search.query.AbstractQueryToElastic;
import com.phonepe.commons.columbus.elastic.search.query.ElasticQueryTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.query.term.FieldExistsQuery;
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
