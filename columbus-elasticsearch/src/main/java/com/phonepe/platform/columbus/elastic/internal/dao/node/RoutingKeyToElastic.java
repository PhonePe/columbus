package com.phonepe.platform.columbus.elastic.internal.dao.node;

import com.phonepe.platform.columbus.common.Elements;
import com.phonepe.platform.columbus.dao.node.RoutingKey;
import com.phonepe.platform.columbus.elastic.dao.node.ElasticNode;
import com.phonepe.platform.columbus.elastic.dao.node.ElasticNodeIdResolutionContext;
import com.phonepe.platform.columbus.elastic.dao.node.NodeIdToElastic;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;

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
