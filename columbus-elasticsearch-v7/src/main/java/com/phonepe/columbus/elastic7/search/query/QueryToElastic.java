package com.phonepe.columbus.elastic7.search.query;

import com.phonepe.columbus.search.query.Query;
import com.phonepe.columbus.search.query.visitor.QueryTransformationVisitor;
import org.elasticsearch.index.query.QueryBuilder;

/**
 * An interface to transform a specific {@link Query} to its internal representation for ElasticSearch.
 *
 * @param <T> the type of the {@link Query} being transformed.
 * @param <C> the type of the context required for transformation.
 */
public interface QueryToElastic<T extends Query, C extends ElasticQueryTransformationContext<C>>
        extends QueryTransformationVisitor<QueryBuilder, T, Object, C> {
}
