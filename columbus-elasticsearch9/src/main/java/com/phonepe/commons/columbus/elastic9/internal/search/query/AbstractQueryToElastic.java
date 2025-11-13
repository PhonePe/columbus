package com.phonepe.commons.columbus.elastic9.internal.search.query;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryBase;
import com.phonepe.commons.columbus.elastic9.search.query.ElasticQueryTransformationContext;
import com.phonepe.commons.columbus.elastic9.search.query.QueryToElastic;
import com.phonepe.commons.columbus.search.query.AbstractQuery;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

public abstract class AbstractQueryToElastic<T extends AbstractQuery, C extends ElasticQueryTransformationContext<C>>
        implements QueryToElastic<T, C> {

    protected void applyCommonProperties(final T query, final QueryBase.AbstractBuilder<?> builder) {
        applyIfNotNull(query.getName(), builder::queryName);
        applyIfNotNull(query.getBoost(), builder::boost);
    }
}
