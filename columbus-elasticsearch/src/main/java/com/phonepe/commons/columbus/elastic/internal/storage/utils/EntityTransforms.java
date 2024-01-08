package com.phonepe.commons.columbus.elastic.internal.storage.utils;

import com.phonepe.commons.columbus.elastic.dao.node.ElasticNode;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class EntityTransforms {

    public String getSingleRouting(final ElasticNode node) {
        if (node.getRouting().size() > 1) {
            log.warn("Expected single value for routing, found {}. Using shard: {}",
                    node.getRouting().size(), node.getShard());
        }
        return node.getShard();
    }
}
