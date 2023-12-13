package com.phonepe.platform.columbus.search.page;

/**
 * An interface that transforms a given {@link PageBounds} to the corresponding internal representation.
 *
 * @param <R> represents the type used by storage.
 * @param <C> represents the additional context required for resolution.
 */
public interface PageResolver<R, C> {

    R resolve(PageBounds page, C context);

    R resolve(PageBounds page);
}
