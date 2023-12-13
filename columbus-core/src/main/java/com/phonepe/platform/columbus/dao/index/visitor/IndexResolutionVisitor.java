package com.phonepe.platform.columbus.dao.index.visitor;

import com.phonepe.platform.columbus.dao.index.Index;
import com.phonepe.platform.columbus.visitor.impl.EntityVisitor;

/**
 * An interface to resolve a specific {@link Index} to its corresponding internal representation as required by the
 * storage.
 *
 * @param <R> represents the type used by storage.
 * @param <T> the type of the {@link Index} being resolved.
 * @param <P> represents the additional context required for resolution.
 * @param <C> the type of the context required for resolution.
 */
public interface IndexResolutionVisitor<R, T extends Index, P, C extends IndexResolutionContext<R, P, C>>
        extends EntityVisitor<R, T, Index, P, C> {

    @Override
    R visit(T index, P props, C context);
}
