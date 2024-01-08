package com.phonepe.commons.columbus.elastic.storage.utils;

import com.phonepe.commons.columbus.storage.commands.HealthCheckCommand.HealthStatus;
import com.phonepe.commons.columbus.storage.models.RequestExecutionPolicy;
import com.phonepe.commons.columbus.storage.models.result.UpsertResult;
import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.phonepe.commons.columbus.elastic.internal.storage.utils.EnumTransforms.toHealthStatus;
import static com.phonepe.commons.columbus.elastic.internal.storage.utils.EnumTransforms.toRefreshPolicy;
import static com.phonepe.commons.columbus.elastic.internal.storage.utils.EnumTransforms.toUpsertResult;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnumTransformsTest {

    @Test
    @DisplayName("valid health status transformations")
    void testHealthStatusTransforms() {
        assertEquals(HealthStatus.HEALTHY, toHealthStatus(ClusterHealthStatus.GREEN));
        assertEquals(HealthStatus.DEGRADED, toHealthStatus(ClusterHealthStatus.YELLOW));
        assertEquals(HealthStatus.UNHEALTHY, toHealthStatus(ClusterHealthStatus.RED));
    }

    @Test
    @DisplayName("valid upsert result transformations")
    void testUpsertResultTransforms() {
        assertEquals(UpsertResult.CREATED, toUpsertResult(Result.CREATED));
        assertEquals(UpsertResult.UPDATED, toUpsertResult(Result.UPDATED));
    }

    @Test
    @DisplayName("invalid upsert result transformations")
    void testInvalidUpsertResultTransforms() {
        assertThrows(IllegalArgumentException.class, () -> toUpsertResult(Result.DELETED));
        assertThrows(IllegalArgumentException.class, () -> toUpsertResult(Result.NOT_FOUND));
        assertThrows(IllegalArgumentException.class, () -> toUpsertResult(Result.NOOP));
    }

    @Test
    @DisplayName("valid request execution policy transformations")
    void testRequestExecutionPolicyTransforms() {
        assertEquals(RefreshPolicy.NONE, toRefreshPolicy(RequestExecutionPolicy.SUBMIT));
        assertEquals(RefreshPolicy.WAIT_UNTIL, toRefreshPolicy(RequestExecutionPolicy.WAIT_FOR_COMPLETION));
        assertEquals(RefreshPolicy.IMMEDIATE, toRefreshPolicy(RequestExecutionPolicy.FORCE_COMPLETION));
    }
}
