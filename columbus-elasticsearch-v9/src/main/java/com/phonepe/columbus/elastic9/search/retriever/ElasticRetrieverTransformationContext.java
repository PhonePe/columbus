package com.phonepe.columbus.elastic9.search.retriever;

import co.elastic.clients.elasticsearch._types.Retriever;
import com.phonepe.columbus.elastic9.storage.toolkit.ElasticToolkit;
import com.phonepe.columbus.search.retriever.visitor.RetrieverTransformationContext;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * The context used to transform {@link com.phonepe.columbus.search.retriever.Retriever Retrievers} as needed by
 * ElasticSearch.
 *
 * @param <C> self reference.
 */
@Getter
@Accessors(fluent = true)
@SuperBuilder(toBuilder = true)
public abstract class ElasticRetrieverTransformationContext<C extends ElasticRetrieverTransformationContext<C>>
        extends RetrieverTransformationContext<Retriever, Object, C> {

    @NonNull
    private final ElasticToolkit toolkit;
}
