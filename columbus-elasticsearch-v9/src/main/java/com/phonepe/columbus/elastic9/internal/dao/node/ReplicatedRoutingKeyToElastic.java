package com.phonepe.columbus.elastic9.internal.dao.node;

import com.phonepe.columbus.common.Elements;
import com.phonepe.columbus.dao.node.ReplicatedRoutingKey;
import com.phonepe.columbus.elastic9.dao.node.ElasticNode;
import com.phonepe.columbus.elastic9.dao.node.ElasticNodeIdResolutionContext;
import com.phonepe.columbus.elastic9.dao.node.NodeIdToElastic;
import com.phonepe.columbus.registry.factory.RegistryKey;

@RegistryKey(name = ReplicatedRoutingKey.TYPE, type = ReplicatedRoutingKey.class)
public class ReplicatedRoutingKeyToElastic<C extends ElasticNodeIdResolutionContext<C>>
        implements NodeIdToElastic<ReplicatedRoutingKey, C> {

    @Override
    public ElasticNode visit(final ReplicatedRoutingKey node, final Object props, final C context) {
        return ElasticNode.builder()
                .routing(node.getKeys())
                .shard(Elements.firstOrNull(node.getKeys()))
                .preference(node.getReplica())
                .build();
    }
}
