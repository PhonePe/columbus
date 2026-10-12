package com.phonepe.columbus.elastic9.search.query;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import com.phonepe.columbus.elastic9.storage.toolkit.ElasticToolkit;
import com.phonepe.columbus.search.query.visitor.QueryTransformationContext;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

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
        extends QueryTransformationContext<QueryVariant, Object, C> {

    @NonNull
    private final ElasticToolkit toolkit;
}
