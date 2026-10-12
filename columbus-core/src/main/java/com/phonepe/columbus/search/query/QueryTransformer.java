package com.phonepe.columbus.search.query;

/**
 * An interface that transforms a given {@link Query} to the corresponding internal representation.
 *
 * @param <R> represents the type used by storage.
 * @param <C> represents the additional context required for transformation.
 */
public interface QueryTransformer<R, C> {

    R transform(Query query, C context);

    R transform(Query query);
}
