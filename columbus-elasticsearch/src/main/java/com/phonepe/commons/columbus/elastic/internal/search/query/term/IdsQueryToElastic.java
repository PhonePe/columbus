package com.phonepe.commons.columbus.elastic.internal.search.query.term;

import com.phonepe.commons.columbus.elastic.dao.id.ElasticId;
import com.phonepe.commons.columbus.elastic.internal.search.query.AbstractQueryToElastic;
import com.phonepe.commons.columbus.elastic.search.query.ElasticQueryTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.query.term.IdsQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

@RegistryKey(name = IdsQuery.TYPE, type = IdsQuery.class)
public class IdsQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<IdsQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final IdsQuery query, final Object props, final C context) {
        final var idResolver = context.toolkit().idResolver();
        return QueryBuilders.idsQuery()
                .addIds(query.getIds().stream()
                        .map(id -> idResolver.resolve(id, props))
                        .map(ElasticId::getId)
                        .toArray(String[]::new));
    }
}
