package com.phonepe.commons.columbus.elastic.internal.search.query;

import com.phonepe.commons.columbus.elastic.search.query.ElasticQueryTransformationContext;
import com.phonepe.commons.columbus.elastic.search.query.QueryToElastic;
import com.phonepe.commons.columbus.search.query.AbstractQuery;
import org.elasticsearch.index.query.QueryBuilder;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

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
