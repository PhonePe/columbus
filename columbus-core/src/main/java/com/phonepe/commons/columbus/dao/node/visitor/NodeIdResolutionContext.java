package com.phonepe.commons.columbus.dao.node.visitor;

import com.phonepe.commons.columbus.dao.node.NodeId;
import com.phonepe.commons.columbus.dao.node.NodeIdResolver;
import com.phonepe.commons.columbus.visitor.impl.VisitorContext;
import lombok.experimental.SuperBuilder;

/**
 * A {@link NodeIdResolver} that uses visitor to convert {@link NodeId} to the corresponding type.
 *
 * @param <R> represents the type used by storage.
 * @param <P> represents the additional context required for resolution.
 * @param <C> self reference, the context used by the resolvers.
 */
@SuperBuilder(toBuilder = true)
public abstract class NodeIdResolutionContext<R, P, C extends NodeIdResolutionContext<R, P, C>>
        extends VisitorContext<R, NodeId, P, C> implements NodeIdResolver<R, P> {

    @Override
    public R resolve(final NodeId node, final P context) {
        return visit(node, context);
    }

    @Override
    public R resolve(final NodeId node) {
        return visit(node);
    }
}
