package com.phonepe.platform.columbus.dao.node;

/**
 * An interface that transforms a given {@link NodeId} to the corresponding internal representation.
 *
 * @param <R> represents the type used by storage.
 */
public interface NodeIdResolver<R> {

    R resolve(NodeId node);
}
