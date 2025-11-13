package com.phonepe.commons.columbus.elastic9.internal.dao.node;

import com.phonepe.commons.columbus.dao.node.ShardId;
import com.phonepe.commons.columbus.elastic9.dao.node.ElasticNode;
import com.phonepe.commons.columbus.elastic9.dao.node.ElasticNodeIdResolutionContext;
import com.phonepe.commons.columbus.elastic9.dao.node.NodeIdToElastic;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;

@RegistryKey(name = ShardId.TYPE, type = ShardId.class)
public class ShardIdToElastic<C extends ElasticNodeIdResolutionContext<C>> implements NodeIdToElastic<ShardId, C> {

    @Override
    public ElasticNode visit(final ShardId node, final Object props, final C context) {
        return ElasticNode.builder()
                .shard(node.getShard())
                .routing(node.getShard())
                .build();
    }
}
