package com.phonepe.columbus.elastic7.internal.search.query;

import com.phonepe.columbus.elastic7.search.query.ElasticQueryTransformationContext;
import com.phonepe.columbus.elastic7.search.query.QueryToElastic;
import com.phonepe.columbus.search.query.AbstractQuery;
import org.elasticsearch.index.query.QueryBuilder;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

public abstract class AbstractQueryToElastic<T extends AbstractQuery, C extends ElasticQueryTransformationContext<C>>
        implements QueryToElastic<T, C> {

    @Override
    public QueryBuilder visit(final T query, final Object props, final C context) {
        final var builder = buildQuery(query, props, context);
        applyIfNotNull(query.getName(), builder::queryName);
        applyIfNotNull(query.getBoost(), builder::boost);
        return builder;
    }

    protected abstract QueryBuilder buildQuery(final T query, final Object props, final C context);
}
