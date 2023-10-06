package com.phonepe.platform.columbus.dao.id;

/**
 * An interface that transforms a given {@link Id} to the corresponding internal representation.
 *
 * @param <R> represents the type used by storage.
 */
public interface IdResolver<R> {

    R resolve(Id id);
}
