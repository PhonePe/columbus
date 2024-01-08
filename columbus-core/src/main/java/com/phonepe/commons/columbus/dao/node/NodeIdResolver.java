package com.phonepe.commons.columbus.dao.node;

/**
 * An interface that transforms a given {@link NodeId} to the corresponding internal representation.
 *
 * @param <R> represents the type used by storage.
 * @param <C> represents the additional context required for resolution.
 */
public interface NodeIdResolver<R, C> {

    R resolve(NodeId node, C context);

    R resolve(NodeId node);
}
