package com.phonepe.platform.columbus.search.page.visitor;

import com.phonepe.platform.columbus.search.page.PageBounds;
import com.phonepe.platform.columbus.visitor.impl.EntityVisitor;

/**
 * An interface to resolve a specific {@link PageBounds} to its corresponding internal representation as required by the
 * storage.
 *
 * @param <R> represents the type used by storage.
 * @param <T> the type of the {@link PageBounds} being resolved.
 * @param <C> the type of the context required for resolution.
 */
public interface PageResolutionVisitor<R, T extends PageBounds, C extends PageResolutionContext<R, C>>
        extends EntityVisitor<R, T, PageBounds, C> {

    @Override
    R visit(T page, C context);
}
