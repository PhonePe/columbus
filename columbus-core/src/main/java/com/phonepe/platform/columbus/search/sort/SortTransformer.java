package com.phonepe.platform.columbus.search.sort;

/**
 * An interface that transforms a given {@link Sort} to the corresponding internal representation.
 *
 * @param <R> represents the type used by storage.
 * @param <C> represents the additional context required for transformation.
 */
public interface SortTransformer<R, C> {

    R transform(Sort sort, C context);

    R transform(Sort sort);
}
