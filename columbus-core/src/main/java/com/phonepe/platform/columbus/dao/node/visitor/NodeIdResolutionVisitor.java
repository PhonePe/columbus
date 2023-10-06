package com.phonepe.platform.columbus.dao.node.visitor;

import com.phonepe.platform.columbus.dao.node.NodeId;
import com.phonepe.platform.columbus.visitor.impl.EntityVisitor;

/**
 * An interface to resolve a specific {@link NodeId} to its corresponding internal representation as required by the
 * storage.
 *
 * @param <R> represents the type used by storage.
 * @param <T> the type of the {@link NodeId} being resolved.
 * @param <C> the type of the context required for resolution.
 */
public interface NodeIdResolutionVisitor<R, T extends NodeId, C extends NodeIdResolutionContext<R, C>>
        extends EntityVisitor<R, T, NodeId, C> {

    @Override
    R visit(T node, C context);
}
