package com.phonepe.columbus.search.retriever;

/**
 * An interface that transforms a given {@link Retriever} to the corresponding internal representation.
 *
 * @param <R> represents the type used by storage.
 * @param <C> represents the additional context required for transformation.
 */
public interface RetrieverTransformer<R, C> {

    R transform(Retriever retriever, C context);

    R transform(Retriever retriever);
}
