/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
