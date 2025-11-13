package com.phonepe.commons.columbus.elastic9.internal.search.query.term;

import co.elastic.clients.elasticsearch._types.query_dsl.PrefixQuery.Builder;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import com.phonepe.commons.columbus.elastic9.internal.search.query.AbstractQueryToElastic;
import com.phonepe.commons.columbus.elastic9.search.query.ElasticQueryTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.query.term.PrefixQuery;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = PrefixQuery.TYPE, type = PrefixQuery.class)
public class PrefixQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<PrefixQuery, C> {

    @Override
    public QueryVariant visit(final PrefixQuery query, final Object props, final C context) {
        final var builder = new Builder()
                .field(query.getField())
                .value(query.getValue());
        applyCommonProperties(query, builder);
        applyIfNotNull(query.getCaseInsensitive(), builder::caseInsensitive);
        applyIfNotNull(query.getRewrite(), builder::rewrite);
        return builder.build();
    }
}
