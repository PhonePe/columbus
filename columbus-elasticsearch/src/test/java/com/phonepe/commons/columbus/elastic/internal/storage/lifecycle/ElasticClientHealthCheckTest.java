package com.phonepe.commons.columbus.elastic.internal.storage.lifecycle;

import com.phonepe.commons.columbus.elastic.storage.ElasticClient;
import com.phonepe.commons.columbus.storage.commands.HealthCheckCommand.HealthStatus;
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
