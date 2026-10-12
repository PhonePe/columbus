package com.phonepe.columbus.dao.id;

/**
 * An interface that transforms a given {@link Id} to the corresponding internal representation.
 *
 * @param <R> represents the type used by storage.
 * @param <C> represents the additional context required for resolution.
 */
public interface IdResolver<R, C> {

    R resolve(Id id, C context);

    R resolve(Id id);
}
