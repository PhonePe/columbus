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
package com.phonepe.columbus.elastic7.storage.utils;

import com.phonepe.columbus.storage.commands.HealthCheckCommand.HealthStatus;
import com.phonepe.columbus.storage.models.RequestExecutionPolicy;
import com.phonepe.columbus.storage.models.result.UpsertResult;
import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.phonepe.columbus.elastic7.internal.storage.utils.EnumTransforms.toHealthStatus;
import static com.phonepe.columbus.elastic7.internal.storage.utils.EnumTransforms.toRefreshPolicy;
import static com.phonepe.columbus.elastic7.internal.storage.utils.EnumTransforms.toUpsertResult;
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
        assertEquals(UpsertResult.UNALTERED, toUpsertResult(Result.NOOP));
    }

    @Test
    @DisplayName("invalid upsert result transformations")
    void testInvalidUpsertResultTransforms() {
        assertThrows(IllegalArgumentException.class, () -> toUpsertResult(Result.DELETED));
        assertThrows(IllegalArgumentException.class, () -> toUpsertResult(Result.NOT_FOUND));
    }

    @Test
    @DisplayName("valid request execution policy transformations")
    void testRequestExecutionPolicyTransforms() {
        assertEquals(RefreshPolicy.NONE, toRefreshPolicy(RequestExecutionPolicy.SUBMIT));
        assertEquals(RefreshPolicy.WAIT_UNTIL, toRefreshPolicy(RequestExecutionPolicy.WAIT_FOR_COMPLETION));
        assertEquals(RefreshPolicy.IMMEDIATE, toRefreshPolicy(RequestExecutionPolicy.FORCE_COMPLETION));
    }
}
