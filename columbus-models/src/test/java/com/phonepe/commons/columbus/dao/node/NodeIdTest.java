package com.phonepe.commons.columbus.dao.node;

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
