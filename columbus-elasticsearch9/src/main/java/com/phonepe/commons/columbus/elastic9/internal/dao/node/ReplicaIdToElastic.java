package com.phonepe.commons.columbus.elastic9.internal.dao.node;

import com.phonepe.commons.columbus.dao.node.ReplicaId;
import com.phonepe.commons.columbus.elastic9.dao.node.ElasticNode;
import com.phonepe.commons.columbus.elastic9.dao.node.ElasticNodeIdResolutionContext;
import com.phonepe.commons.columbus.elastic9.dao.node.NodeIdToElastic;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;

@RegistryKey(name = ReplicaId.TYPE, type = ReplicaId.class)
public class ReplicaIdToElastic<C extends ElasticNodeIdResolutionContext<C>> implements NodeIdToElastic<ReplicaId, C> {

    @Override
    public ElasticNode visit(final ReplicaId node, final Object props, final C context) {
        return ElasticNode.builder()
                .preference(node.getReplica())
                .build();
    }
}
