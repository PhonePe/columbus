package com.phonepe.platform.columbus.elastic.storage.utils;

import com.phonepe.platform.columbus.storage.commands.HealthCheckCommand.HealthStatus;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnumTransformsTest {

    @Test
    @DisplayName("valid health status transformations")
    void testHealthStatusTransforms() {
        assertEquals(HealthStatus.HEALTHY, EnumTransforms.toHealthStatus(ClusterHealthStatus.GREEN));
        assertEquals(HealthStatus.DEGRADED, EnumTransforms.toHealthStatus(ClusterHealthStatus.YELLOW));
        assertEquals(HealthStatus.UNHEALTHY, EnumTransforms.toHealthStatus(ClusterHealthStatus.RED));
    }

    @Test
    @DisplayName("invalid health status transformation")
    void testInvalidHealthStatusTransform() {
        //noinspection DataFlowIssue
        assertThrows(NullPointerException.class, () -> EnumTransforms.toHealthStatus(null));
    }
}
