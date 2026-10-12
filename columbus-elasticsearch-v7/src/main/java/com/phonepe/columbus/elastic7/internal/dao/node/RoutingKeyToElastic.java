package com.phonepe.columbus.elastic7.internal.dao.node;

import com.phonepe.columbus.common.Elements;
import com.phonepe.columbus.dao.node.RoutingKey;
import com.phonepe.columbus.elastic7.dao.node.ElasticNode;
import com.phonepe.columbus.elastic7.dao.node.ElasticNodeIdResolutionContext;
import com.phonepe.columbus.elastic7.dao.node.NodeIdToElastic;
import com.phonepe.columbus.registry.factory.RegistryKey;

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
