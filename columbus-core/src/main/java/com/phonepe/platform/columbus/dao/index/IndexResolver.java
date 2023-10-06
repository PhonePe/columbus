package com.phonepe.platform.columbus.dao.index;

/**
 * An interface that transforms a given {@link Index} to the corresponding internal representation.
 *
 * @param <R> represents the type used by storage.
 */
public interface IndexResolver<R> {

    R resolve(Index index);
}
