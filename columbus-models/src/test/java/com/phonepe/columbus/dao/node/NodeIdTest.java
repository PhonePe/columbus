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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NodeIdTest {

    @Test
    @DisplayName("replica")
    void testReplicaId() {
        final var nodeId = NodeIds.replica("replica");
        assertAll(
                "replica",
                () -> assertEquals(ReplicaId.TYPE, nodeId.getType(), "must match type"),
                () -> assertEquals("replica", nodeId.getReplica(), "must contain provided id"));
    }

    @Test
    @DisplayName("shard")
    void testShardId() {
        final var nodeId = NodeIds.shard("shard");
        assertAll(
                "shard",
                () -> assertEquals(ShardId.TYPE, nodeId.getType(), "must match type"),
                () -> assertEquals("shard", nodeId.getShard(), "must contain provided id"));
    }

    @Test
    @DisplayName("routing key")
    void testRoutingKey() {
        final var keys = List.of("route");
        final var nodeId = NodeIds.routingKeys(keys);
        assertAll(
                "routing key",
                () -> assertEquals(RoutingKey.TYPE, nodeId.getType(), "must match type"),
                () -> assertEquals(keys, nodeId.getKeys(), "must contain provided id"));
    }

    @Test
    @DisplayName("replicated shard")
    void testReplicatedShardId() {
        final var nodeId = NodeIds.shard("shard", "replica");
        assertAll(
                "replicated shard",
                () -> assertEquals(ReplicatedShardId.TYPE, nodeId.getType(), "must match type"),
                () -> assertEquals("shard", nodeId.getShard(), "must contain provided id"),
                () -> assertEquals("replica", nodeId.getReplica(), "must contain provided replica"));
    }

    @Test
    @DisplayName("replicated routing key")
    void testReplicatedRoutingKey() {
        final var keys = List.of("route");
        final var nodeId = NodeIds.routingKeys(keys, "replica");
        assertAll(
                "replicated routing key",
                () -> assertEquals(ReplicatedRoutingKey.TYPE, nodeId.getType(), "must match type"),
                () -> assertEquals(keys, nodeId.getKeys(), "must contain provided id"),
                () -> assertEquals("replica", nodeId.getReplica(), "must contain provided replica"));
    }
}
