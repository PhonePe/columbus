package com.phonepe.platform.columbus.elastic.dao.node;

import com.phonepe.platform.columbus.dao.node.NodeId;
import com.phonepe.platform.columbus.dao.node.visitor.NodeIdResolutionVisitor;

/**
 * An interface to resolve a specific {@link NodeId} to its internal representation for ElasticSearch.
 *
 * @param <T> the type of the {@link NodeId} being resolved.
 * @param <C> the type of the context required for resolution.
 */
public interface NodeIdToElastic<T extends NodeId, C extends ElasticNodeIdResolutionContext<C>>
        extends NodeIdResolutionVisitor<ElasticNode, T, Object, C> {
}
