package com.phonepe.commons.columbus.search.page.visitor;

import com.phonepe.commons.columbus.search.page.PageBounds;
import com.phonepe.commons.columbus.visitor.impl.EntityVisitor;

/**
 * An interface to resolve a specific {@link PageBounds} to its corresponding internal representation as required by the
 * storage.
 *
 * @param <R> represents the type used by storage.
 * @param <T> the type of the {@link PageBounds} being resolved.
 * @param <P> represents the additional context required for resolution.
 * @param <C> the type of the context required for resolution.
 */
public interface PageResolutionVisitor<R, T extends PageBounds, P, C extends PageResolutionContext<R, P, C>>
        extends EntityVisitor<R, T, PageBounds, P, C> {

    @Override
    R visit(T page, P props, C context);
}
