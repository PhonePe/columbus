package com.phonepe.platform.columbus.elastic.internal.search.query;

import com.phonepe.platform.columbus.elastic.search.query.ElasticQueryTransformationContext;
import com.phonepe.platform.columbus.elastic.search.query.QueryToElastic;
import com.phonepe.platform.columbus.search.query.BaseQuery;
import org.elasticsearch.index.query.QueryBuilder;

import static com.phonepe.platform.columbus.common.Predicates.applyIfNotNull;

public abstract class BaseQueryToElastic<T extends BaseQuery, C extends ElasticQueryTransformationContext<C>>
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
