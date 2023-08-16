package com.phonepe.platform.columbus.elastic.internal.storage.lifecycle;

import com.phonepe.platform.columbus.elastic.storage.ElasticClient;
import com.phonepe.platform.columbus.storage.commands.HealthCheckCommand.HealthStatus;
import com.phonepe.platform.columbus.test.extensions.WithMockito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@WithMockito
class ElasticClientHealthCheckTest {

    @Mock
    private ElasticClient<?> client;

    private ElasticClientHealthCheck healthCheck;

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
