package com.phonepe.platform.columbus.search.query;

/**
 * An interface that transforms a given {@link Query} to the corresponding internal representation.
 *
 * @param <R> represents the type used by storage.
 */
public interface QueryTransformer<R> {

    R transform(Query query);
}
