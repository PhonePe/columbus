package com.phonepe.commons.columbus.elastic.internal.dao.node;

import com.phonepe.commons.columbus.common.Elements;
import com.phonepe.commons.columbus.dao.node.ReplicatedRoutingKey;
import com.phonepe.commons.columbus.elastic.dao.node.ElasticNode;
import com.phonepe.commons.columbus.elastic.dao.node.ElasticNodeIdResolutionContext;
import com.phonepe.commons.columbus.elastic.dao.node.NodeIdToElastic;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;

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
