package com.phonepe.platform.columbus.elastic.internal.dao.impl;

import com.phonepe.platform.columbus.core.toolkit.Toolkit;
import com.phonepe.platform.columbus.core.utils.RegistryCreators;
import com.phonepe.platform.columbus.dao.node.NodeId;
import com.phonepe.platform.columbus.dao.node.NodeIdResolver;
import com.phonepe.platform.columbus.dao.node.NodeIds;
import com.phonepe.platform.columbus.elastic.dao.node.ElasticNode;
import com.phonepe.platform.columbus.elastic.dao.node.NodeIdToElastic;
import com.phonepe.platform.columbus.reflect.Generics;
import com.phonepe.platform.columbus.test.annotations.WithToolkit;
import com.phonepe.platform.columbus.visitor.VisitorHelpers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WithToolkit.All
class ElasticNodeIdResolverTest {

    private static final String REPLICA = "replica";
    private static final String SHARD = "shard";
    private static final String ROUTING = "routing";
    static NodeIdResolver<ElasticNode> nodeIdResolver;

    @Test
    @DisplayName("replica")
    void testReplica() {
        final var nodeId = NodeIds.replica(REPLICA);
        final var expected = ElasticNode.builder()
                .preference(REPLICA)
                .build();

        final var elasticNode = nodeIdResolver.resolve(nodeId);

        assertEquals(expected, elasticNode, "node id must match");
    }

    @Test
    @DisplayName("shard id")
    void testShardId() {
        final var nodeId = NodeIds.shard(SHARD);
        final var expected = ElasticNode.builder()
                .shard(SHARD)
                .routing(SHARD)
                .build();

        final var elasticNode = nodeIdResolver.resolve(nodeId);

        assertEquals(expected, elasticNode, "node id must match");
    }

    @Test
    @DisplayName("routing key")
    void testRoutingKey() {
        final var nodeId = NodeIds.routingKeys(List.of(SHARD, ROUTING));
        final var expected = ElasticNode.builder()
                .shard(SHARD)
                .routing(SHARD)
                .routing(ROUTING)
                .build();

        final var elasticNode = nodeIdResolver.resolve(nodeId);

        assertEquals(expected, elasticNode, "node id must match");
    }

    @Test
    @DisplayName("replicated shard id")
    void testReplicatedShardId() {
        final var nodeId = NodeIds.shard(SHARD, REPLICA);
        final var expected = ElasticNode.builder()
                .shard(SHARD)
                .routing(SHARD)
                .preference(REPLICA)
                .build();

        final var elasticNode = nodeIdResolver.resolve(nodeId);

        assertEquals(expected, elasticNode, "node id must match");
    }

    @Test
    @DisplayName("replicated routing key")
    void testReplicatedRoutingKey() {
        final var nodeId = NodeIds.routingKeys(List.of(SHARD, ROUTING), REPLICA);
        final var expected = ElasticNode.builder()
                .shard(SHARD)
                .routing(SHARD)
                .routing(ROUTING)
                .preference(REPLICA)
                .build();

        final var elasticNode = nodeIdResolver.resolve(nodeId);

        assertEquals(expected, elasticNode, "node id must match");
    }

    @BeforeAll
    static void beforeAll(final Toolkit toolkit) {
        nodeIdResolver = ElasticNodeIdResolver.builder()
                .resolver(VisitorHelpers.resolver(RegistryCreators.createVisitorRegistry(
                        toolkit.visitorFactory(),
                        Generics.<NodeIdToElastic<NodeId, ElasticNodeIdResolver>>typed(NodeIdToElastic.class),
                        ElasticNodeIdResolver.class,
                        NodeId.class,
                        NodeId::getType,
                        null)))
                .build();
    }
}
