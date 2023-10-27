package com.phonepe.platform.columbus.dao.node.visitor;

import com.phonepe.platform.columbus.dao.node.NodeId;
import com.phonepe.platform.columbus.dao.node.NodeIdResolver;
import com.phonepe.platform.columbus.visitor.impl.VisitorContext;
import lombok.experimental.SuperBuilder;

/**
 * A {@link NodeIdResolver} that uses visitor to convert {@link NodeId} to the corresponding type.
 *
 * @param <R> represents the type used by storage.
 * @param <C> self reference, the context used by the resolvers.
 */
@SuperBuilder(toBuilder = true)
public abstract class NodeIdResolutionContext<R, C extends NodeIdResolutionContext<R, C>>
        extends VisitorContext<R, NodeId, C> implements NodeIdResolver<R> {

    @Override
    public R resolve(final NodeId node) {
        return visit(node);
    }
}
