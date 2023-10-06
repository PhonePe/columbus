package com.phonepe.platform.columbus.elastic.internal.dao.node;

import com.phonepe.platform.columbus.dao.node.ShardId;
import com.phonepe.platform.columbus.elastic.dao.node.ElasticNode;
import com.phonepe.platform.columbus.elastic.dao.node.ElasticNodeIdResolutionContext;
import com.phonepe.platform.columbus.elastic.dao.node.NodeIdToElastic;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;

@RegistryKey(name = ShardId.TYPE, type = ShardId.class)
public class ShardIdToElastic<C extends ElasticNodeIdResolutionContext<C>> implements NodeIdToElastic<ShardId, C> {

    @Override
    public ElasticNode visit(final ShardId node, final C context) {
        return ElasticNode.builder()
                .shard(node.getShard())
                .routing(node.getShard())
                .build();
    }
}
