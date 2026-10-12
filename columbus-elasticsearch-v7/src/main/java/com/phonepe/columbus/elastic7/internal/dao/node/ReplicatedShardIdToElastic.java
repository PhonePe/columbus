package com.phonepe.columbus.elastic7.internal.dao.node;

import com.phonepe.columbus.dao.node.ReplicatedShardId;
import com.phonepe.columbus.elastic7.dao.node.ElasticNode;
import com.phonepe.columbus.elastic7.dao.node.ElasticNodeIdResolutionContext;
import com.phonepe.columbus.elastic7.dao.node.NodeIdToElastic;
import com.phonepe.columbus.registry.factory.RegistryKey;

@RegistryKey(name = ReplicatedShardId.TYPE, type = ReplicatedShardId.class)
public class ReplicatedShardIdToElastic<C extends ElasticNodeIdResolutionContext<C>>
        implements NodeIdToElastic<ReplicatedShardId, C> {

    @Override
    public ElasticNode visit(final ReplicatedShardId node, final Object props, final C context) {
        return ElasticNode.builder()
                .preference(node.getReplica())
                .shard(node.getShard())
                .routing(node.getShard())
                .build();
    }
}
