package com.phonepe.platform.columbus.search.page;

/**
 * An interface that transforms a given {@link PageBounds} to the corresponding internal representation.
 *
 * @param <R> represents the type used by storage.
 */
public interface PageResolver<R> {

    R resolve(PageBounds page);
}
