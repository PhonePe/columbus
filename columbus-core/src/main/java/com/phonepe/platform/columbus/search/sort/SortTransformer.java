package com.phonepe.platform.columbus.search.sort;

/**
 * An interface that transforms a given {@link Sort} to the corresponding internal representation.
 *
 * @param <R> represents the type used by storage.
 */
public interface SortTransformer<R> {

    R transform(Sort sort);
}
