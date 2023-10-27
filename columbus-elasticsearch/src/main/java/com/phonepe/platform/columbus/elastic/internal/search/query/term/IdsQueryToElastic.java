package com.phonepe.platform.columbus.elastic.internal.search.query.term;

import com.phonepe.platform.columbus.elastic.dao.id.ElasticId;
import com.phonepe.platform.columbus.elastic.internal.search.query.BaseQueryToElastic;
import com.phonepe.platform.columbus.elastic.search.query.ElasticQueryTransformationContext;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.query.term.IdsQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

@RegistryKey(name = IdsQuery.TYPE, type = IdsQuery.class)
public class IdsQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends BaseQueryToElastic<IdsQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final IdsQuery query, final C context) {
        return QueryBuilders.idsQuery()
                .addIds(query.getIds().stream()
                        .map(context.toolkit().idResolver()::resolve)
                        .map(ElasticId::getId)
                        .toArray(String[]::new));
    }
}
