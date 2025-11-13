package com.phonepe.commons.columbus.elastic9.storage.utils;

import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch._types.Result;
import com.phonepe.commons.columbus.storage.commands.HealthCheckCommand.HealthStatus;
import com.phonepe.commons.columbus.storage.models.RequestExecutionPolicy;
import com.phonepe.commons.columbus.storage.models.result.UpsertResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.phonepe.commons.columbus.elastic9.internal.storage.utils.EnumTransforms.toHealthStatus;
import static com.phonepe.commons.columbus.elastic9.internal.storage.utils.EnumTransforms.toRefreshPolicy;
import static com.phonepe.commons.columbus.elastic9.internal.storage.utils.EnumTransforms.toUpsertResult;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnumTransformsTest {

    @Test
    @DisplayName("valid health status transformations")
    void testHealthStatusTransforms() {
        assertEquals(HealthStatus.HEALTHY, toHealthStatus(co.elastic.clients.elasticsearch._types.HealthStatus.Green));
        assertEquals(HealthStatus.DEGRADED, toHealthStatus(co.elastic.clients.elasticsearch._types.HealthStatus.Yellow));
        assertEquals(HealthStatus.UNHEALTHY, toHealthStatus(co.elastic.clients.elasticsearch._types.HealthStatus.Red));
        assertEquals(HealthStatus.UNHEALTHY, toHealthStatus(co.elastic.clients.elasticsearch._types.HealthStatus.Unavailable));
        assertEquals(HealthStatus.UNHEALTHY, toHealthStatus(co.elastic.clients.elasticsearch._types.HealthStatus.Unknown));
    }

    @Test
    @DisplayName("valid upsert result transformations")
    void testUpsertResultTransforms() {
        assertEquals(UpsertResult.CREATED, toUpsertResult(Result.Created));
        assertEquals(UpsertResult.UPDATED, toUpsertResult(Result.Updated));
        assertEquals(UpsertResult.UNALTERED, toUpsertResult(Result.NoOp));
    }

    @Test
    @DisplayName("invalid upsert result transformations")
    void testInvalidUpsertResultTransforms() {
        assertThrows(IllegalArgumentException.class, () -> toUpsertResult(Result.Deleted));
        assertThrows(IllegalArgumentException.class, () -> toUpsertResult(Result.NotFound));
    }

    @Test
    @DisplayName("valid request execution policy transformations")
    void testRequestExecutionPolicyTransforms() {
        assertEquals(Refresh.False, toRefreshPolicy(RequestExecutionPolicy.SUBMIT));
        assertEquals(Refresh.WaitFor, toRefreshPolicy(RequestExecutionPolicy.WAIT_FOR_COMPLETION));
        assertEquals(Refresh.True, toRefreshPolicy(RequestExecutionPolicy.FORCE_COMPLETION));
    }
}
