package com.phonepe.columbus.elastic9.search.retriever;

import com.phonepe.columbus.search.retriever.Retriever;
import com.phonepe.columbus.search.retriever.visitor.RetrieverTransformationVisitor;

/**
 * An interface to transform a specific {@link Retriever} to its internal representation for ElasticSearch.
 *
 * @param <T> the type of the {@link Retriever} being transformed.
 * @param <C> the type of the context required for transformation.
 */
public interface RetrieverToElastic<T extends Retriever, C extends ElasticRetrieverTransformationContext<C>>
        extends RetrieverTransformationVisitor<co.elastic.clients.elasticsearch._types.Retriever, T, Object, C> {
}
