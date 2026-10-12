package com.phonepe.columbus.dao.node.visitor;

import com.phonepe.columbus.dao.node.NodeId;
import com.phonepe.columbus.visitor.impl.EntityVisitor;

/**
 * An interface to resolve a specific {@link NodeId} to its corresponding internal representation as required by the
 * storage.
 *
 * @param <R> represents the type used by storage.
 * @param <T> the type of the {@link NodeId} being resolved.
 * @param <P> represents the additional context required for resolution.
 * @param <C> the type of the context required for resolution.
 */
public interface NodeIdResolutionVisitor<R, T extends NodeId, P, C extends NodeIdResolutionContext<R, P, C>>
        extends EntityVisitor<R, T, NodeId, P, C> {

    @Override
    R visit(T node, P props, C context);
}
