package com.phonepe.columbus.elastic7.dao.node;

import com.phonepe.columbus.dao.node.visitor.NodeIdResolutionContext;
import lombok.experimental.SuperBuilder;

/**
 * The context used to resolve {@link com.phonepe.columbus.dao.node.NodeId NodeId}s as needed by ElasticSearch.
 *
 * @param <C> self reference.
 */
@SuperBuilder(toBuilder = true)
public abstract class ElasticNodeIdResolutionContext<C extends ElasticNodeIdResolutionContext<C>>
        extends NodeIdResolutionContext<ElasticNode, Object, C> {
}
