package com.phonepe.columbus.elastic9.internal.search.query.special;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import co.elastic.clients.elasticsearch._types.query_dsl.ScriptQuery.Builder;
import com.phonepe.columbus.elastic9.internal.search.query.AbstractQueryToElastic;
import com.phonepe.columbus.elastic9.internal.search.utils.MiscTransforms;
import com.phonepe.columbus.elastic9.search.query.ElasticQueryTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.query.special.ScriptQuery;

import java.util.Objects;

@RegistryKey(name = ScriptQuery.TYPE, type = ScriptQuery.class)
public class ScriptQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<ScriptQuery, C> {

    @Override
    public QueryVariant visit(final ScriptQuery query, final Object props, final C context) {
        final var script = Objects.requireNonNull(query.getScript(), "script must not be null");
        final var builder = new Builder().script(MiscTransforms.toElastic(script));
        applyCommonProperties(query, builder);
        return builder.build();
    }
}
