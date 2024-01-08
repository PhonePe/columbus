package com.phonepe.commons.columbus.dao.index;

/**
 * An interface that transforms a given {@link Index} to the corresponding internal representation.
 *
 * @param <R> represents the type used by storage.
 * @param <C> represents the additional context required for resolution.
 */
public interface IndexResolver<R, C> {

    R resolve(Index index, C context);

    R resolve(Index index);
}
