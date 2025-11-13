package com.phonepe.commons.columbus.elastic9.internal.storage.utils;

import com.phonepe.commons.columbus.dao.id.Id;
import com.phonepe.commons.columbus.dao.index.Index;
import com.phonepe.commons.columbus.dao.node.NodeId;
import com.phonepe.commons.columbus.elastic9.dao.node.ElasticNode;
import com.phonepe.commons.columbus.elastic9.storage.toolkit.ElasticToolkit;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import javax.annotation.Nullable;

@UtilityClass
public class ElasticClientUtils {

    private static final ElasticNode DEFAULT_ELASTIC_NODE = ElasticNode.builder().build();

    public String resolveId(final ElasticToolkit toolkit, final Id id) {
        return toolkit.idResolver().resolve(id).getId();
    }

    public String resolveIndexName(final ElasticToolkit toolkit, final Index index) {
        return toolkit.indexResolver().resolve(index).getName();
    }

    @NonNull
    public ElasticNode resolveNodeId(final ElasticToolkit toolkit, @Nullable final NodeId nodeId) {
        if (nodeId == null) {
            return DEFAULT_ELASTIC_NODE;
        }
        return toolkit.nodeIdResolver().resolve(nodeId);
    }

    public String resolveSingleRouting(final ElasticToolkit toolkit, @Nullable final NodeId nodeId) {
        if (nodeId == null) {
            return null;
        }
        return EntityTransforms.getSingleRouting(resolveNodeId(toolkit, nodeId));
    }
}
