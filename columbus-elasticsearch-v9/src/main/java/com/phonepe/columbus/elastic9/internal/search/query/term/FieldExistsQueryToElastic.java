package com.phonepe.columbus.elastic9.internal.search.query.term;

import co.elastic.clients.elasticsearch._types.query_dsl.ExistsQuery.Builder;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import com.phonepe.columbus.elastic9.internal.search.query.AbstractQueryToElastic;
import com.phonepe.columbus.elastic9.search.query.ElasticQueryTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.query.term.FieldExistsQuery;

@RegistryKey(name = FieldExistsQuery.TYPE, type = FieldExistsQuery.class)
public class FieldExistsQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<FieldExistsQuery, C> {

    @Override
    public QueryVariant visit(final FieldExistsQuery query, final Object props, final C context) {
        final var builder = new Builder()
                .field(query.getField());
        applyCommonProperties(query, builder);
        return builder.build();
    }
}
