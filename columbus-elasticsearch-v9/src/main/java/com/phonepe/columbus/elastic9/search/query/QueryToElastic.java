package com.phonepe.columbus.elastic9.search.query;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import com.phonepe.columbus.search.query.Query;
import com.phonepe.columbus.search.query.visitor.QueryTransformationVisitor;

/**
 * An interface to transform a specific {@link Query} to its internal representation for ElasticSearch.
 *
 * @param <T> the type of the {@link Query} being transformed.
 * @param <C> the type of the context required for transformation.
 */
public interface QueryToElastic<T extends Query, C extends ElasticQueryTransformationContext<C>>
        extends QueryTransformationVisitor<QueryVariant, T, Object, C> {
}
