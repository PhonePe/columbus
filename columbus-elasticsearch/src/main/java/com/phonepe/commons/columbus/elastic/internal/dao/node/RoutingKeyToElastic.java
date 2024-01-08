package com.phonepe.commons.columbus.elastic.internal.dao.node;

import com.phonepe.commons.columbus.common.Elements;
import com.phonepe.commons.columbus.dao.node.RoutingKey;
import com.phonepe.commons.columbus.elastic.dao.node.ElasticNode;
import com.phonepe.commons.columbus.elastic.dao.node.ElasticNodeIdResolutionContext;
import com.phonepe.commons.columbus.elastic.dao.node.NodeIdToElastic;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;

@RegistryKey(name = RoutingKey.TYPE, type = RoutingKey.class)
public class RoutingKeyToElastic<C extends ElasticNodeIdResolutionContext<C>>
        implements NodeIdToElastic<RoutingKey, C> {

    @Override
    public ElasticNode visit(final RoutingKey node, final Object props, final C context) {
        return ElasticNode.builder()
                .routing(node.getKeys())
                .shard(Elements.firstOrNull(node.getKeys()))
                .build();
    }
}
