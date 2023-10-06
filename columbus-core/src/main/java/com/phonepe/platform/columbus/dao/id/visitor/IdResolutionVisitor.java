package com.phonepe.platform.columbus.dao.id.visitor;

import com.phonepe.platform.columbus.dao.id.Id;
import com.phonepe.platform.columbus.visitor.impl.EntityVisitor;

/**
 * An interface to resolve a specific {@link Id} to its corresponding internal representation as required by the
 * storage.
 *
 * @param <R> represents the type used by storage.
 * @param <T> the type of the {@link Id} being resolved.
 * @param <C> the type of the context required for resolution.
 */
public interface IdResolutionVisitor<R, T extends Id, C extends IdResolutionContext<R, C>>
        extends EntityVisitor<R, T, Id, C> {

    @Override
    R visit(T id, C context);
}
