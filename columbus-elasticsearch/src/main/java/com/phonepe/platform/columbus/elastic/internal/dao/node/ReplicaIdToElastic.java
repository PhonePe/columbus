package com.phonepe.platform.columbus.elastic.internal.dao.node;

import com.phonepe.platform.columbus.dao.node.ReplicaId;
import com.phonepe.platform.columbus.elastic.dao.node.ElasticNode;
import com.phonepe.platform.columbus.elastic.dao.node.ElasticNodeIdResolutionContext;
import com.phonepe.platform.columbus.elastic.dao.node.NodeIdToElastic;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;

@RegistryKey(name = ReplicaId.TYPE, type = ReplicaId.class)
public class ReplicaIdToElastic<C extends ElasticNodeIdResolutionContext<C>> implements NodeIdToElastic<ReplicaId, C> {

    @Override
    public ElasticNode visit(final ReplicaId node, final C context) {
        return ElasticNode.builder()
                .preference(node.getReplica())
                .build();
    }
}
