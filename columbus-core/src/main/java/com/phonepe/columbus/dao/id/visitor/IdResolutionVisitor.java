package com.phonepe.columbus.dao.id.visitor;

import com.phonepe.columbus.dao.id.Id;
import com.phonepe.columbus.visitor.impl.EntityVisitor;

/**
 * An interface to resolve a specific {@link Id} to its corresponding internal representation as required by the
 * storage.
 *
 * @param <R> represents the type used by storage.
 * @param <T> the type of the {@link Id} being resolved.
 * @param <P> represents the additional context required for resolution.
 * @param <C> the type of the context required for resolution.
 */
public interface IdResolutionVisitor<R, T extends Id, P, C extends IdResolutionContext<R, P, C>>
        extends EntityVisitor<R, T, Id, P, C> {

    @Override
    R visit(T id, P props, C context);
}
