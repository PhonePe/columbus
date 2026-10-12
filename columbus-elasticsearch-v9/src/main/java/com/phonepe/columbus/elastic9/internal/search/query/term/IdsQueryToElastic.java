package com.phonepe.columbus.elastic9.internal.search.query.term;

import co.elastic.clients.elasticsearch._types.query_dsl.IdsQuery.Builder;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import com.phonepe.columbus.elastic9.dao.id.ElasticId;
import com.phonepe.columbus.elastic9.internal.search.query.AbstractQueryToElastic;
import com.phonepe.columbus.elastic9.search.query.ElasticQueryTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.query.term.IdsQuery;

@RegistryKey(name = IdsQuery.TYPE, type = IdsQuery.class)
public class IdsQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<IdsQuery, C> {

    @Override
    public QueryVariant visit(final IdsQuery query, final Object props, final C context) {
        final var idResolver = context.toolkit().idResolver();
        final var builder = new Builder()
                .values(query.getIds().stream()
                        .map(id -> idResolver.resolve(id, props))
                        .map(ElasticId::getId).toList());

        applyCommonProperties(query, builder);
        return builder.build();
    }
}
