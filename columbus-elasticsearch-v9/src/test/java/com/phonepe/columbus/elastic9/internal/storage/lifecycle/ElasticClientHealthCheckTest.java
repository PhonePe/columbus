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
package com.phonepe.columbus.elastic9.internal.storage.lifecycle;

import com.phonepe.columbus.elastic9.storage.ElasticClient;
import com.phonepe.columbus.storage.commands.HealthCheckCommand.HealthStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ElasticClientHealthCheckTest {

    @Mock
    private ElasticClient<?> client;

    private ElasticClientHealthCheck healthCheck;

    @Nested
    class DegradeFailureTest {

        @BeforeEach
        void setUp() {
            healthCheck = new ElasticClientHealthCheck("health", client, true);
        }

        @Test
        @DisplayName("healthy")
        void testHealthy() {
            doReturn(HealthStatus.HEALTHY).when(client).execute(any());

            final var result = healthCheck.check();

            assertTrue(result.isHealthy(), "must be healthy");
        }

        @Test
        @DisplayName("unhealthy")
        void testUnhealthy() {
            doReturn(HealthStatus.UNHEALTHY).when(client).execute(any());

            final var result = healthCheck.check();

            assertFalse(result.isHealthy(), "must not be healthy");
        }

        @Test
        @DisplayName("unhealthy when degraded")
        void testDegradedUnhealthy() {
            doReturn(HealthStatus.DEGRADED).when(client).execute(any());

            final var result = healthCheck.check();

            assertFalse(result.isHealthy(), "must not be healthy");
        }
    }

    @Nested
    class DegradeAcceptableTest {

        @BeforeEach
        void setUp() {
            healthCheck = new ElasticClientHealthCheck("health", client, false);
        }

        @Test
        @DisplayName("healthy")
        void testHealthy() {
            doReturn(HealthStatus.HEALTHY).when(client).execute(any());

            final var result = healthCheck.check();

            assertTrue(result.isHealthy(), "must be healthy");
        }

        @Test
        @DisplayName("unhealthy")
        void testUnhealthy() {
            doReturn(HealthStatus.UNHEALTHY).when(client).execute(any());

            final var result = healthCheck.check();

            assertFalse(result.isHealthy(), "must not be healthy");
        }

        @Test
        @DisplayName("unhealthy when degraded")
        void testDegradedUnhealthy() {
            doReturn(HealthStatus.DEGRADED).when(client).execute(any());

            final var result = healthCheck.check();

            assertTrue(result.isHealthy(), "must be healthy");
        }
    }
}
