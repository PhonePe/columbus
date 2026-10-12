package com.phonepe.columbus.elastic7.internal.search.query.special;

import com.phonepe.columbus.elastic7.internal.search.query.AbstractQueryToElastic;
import com.phonepe.columbus.elastic7.internal.search.utils.MiscTransforms;
import com.phonepe.columbus.elastic7.search.query.ElasticQueryTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.query.special.ScriptQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.Objects;

@RegistryKey(name = ScriptQuery.TYPE, type = ScriptQuery.class)
public class ScriptQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<ScriptQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final ScriptQuery query, final Object props, final C context) {
        final var script = Objects.requireNonNull(query.getScript(), "script must not be null");
        return QueryBuilders.scriptQuery(MiscTransforms.toElastic(script));
    }
}
