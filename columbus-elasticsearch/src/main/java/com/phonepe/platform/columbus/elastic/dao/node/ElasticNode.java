package com.phonepe.platform.columbus.elastic.dao.node;

import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.Set;

/**
 * Representation of {@link com.phonepe.platform.columbus.dao.node.NodeId NodeId} for ElasticSearch.
 */
@Data
@SuperBuilder
public class ElasticNode {

    private final String shard;

    @Singular(value = "routing", ignoreNullCollections = true)
    private final Set<@NonNull String> routing;

    private final String preference;
}
