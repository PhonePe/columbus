package com.phonepe.columbus.dao.node;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * A factory class to create different types of {@link NodeId} instances.
 */
@UtilityClass
public class NodeIds {

    public ReplicaId replica(final String replica) {
        return ReplicaId.builder()
                .replica(replica)
                .build();
    }

    public ShardId shard(final String shard) {
        return ShardId.builder()
                .shard(shard)
                .build();
    }

    public ReplicatedShardId shard(final String shard, final String replica) {
        return ReplicatedShardId.builder()
                .shard(shard)
                .replica(replica)
                .build();
    }

    public RoutingKey routingKeys(final List<@NonNull String> keys) {
        return RoutingKey.builder()
                .keys(keys)
                .build();
    }

    public ReplicatedRoutingKey routingKeys(final List<@NonNull String> keys, final String replica) {
        return ReplicatedRoutingKey.builder()
                .keys(keys)
                .replica(replica)
                .build();
    }
}
