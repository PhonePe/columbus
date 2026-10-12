package com.phonepe.columbus.elastic7.search.query;

import com.phonepe.columbus.elastic7.storage.toolkit.ElasticToolkit;
import com.phonepe.columbus.search.query.visitor.QueryTransformationContext;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.elasticsearch.index.query.QueryBuilder;

/**
 * The context used to transform {@link com.phonepe.columbus.search.query.Query Queries} as needed by
 * ElasticSearch.
 *
 * @param <C> self reference.
 */
@Getter
@Accessors(fluent = true)
@SuperBuilder(toBuilder = true)
public abstract class ElasticQueryTransformationContext<C extends ElasticQueryTransformationContext<C>>
        extends QueryTransformationContext<QueryBuilder, Object, C> {

    @NonNull
    private final ElasticToolkit toolkit;
}
