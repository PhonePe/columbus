package com.phonepe.columbus.elastic9.storage.handlers;

import co.elastic.clients.elasticsearch.cluster.HealthRequest;
import com.phonepe.columbus.elastic9.internal.storage.ColumbusElasticClient;
import com.phonepe.columbus.elastic9.internal.storage.handlers.HealthCheckHandler;
import com.phonepe.columbus.elastic9.internal.storage.handlers.VersionHandler;
import com.phonepe.columbus.elastic9.test.annotations.WithElasticClient;
import com.phonepe.columbus.elastic9.test.extensions.ElasticContainerExtension;
import com.phonepe.columbus.storage.StorageClient;
import com.phonepe.columbus.storage.commands.HealthCheckCommand;
import com.phonepe.columbus.storage.commands.HealthCheckCommand.HealthStatus;
import com.phonepe.columbus.storage.commands.VersionCommand;
import com.phonepe.columbus.test.annotations.WithMockito;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WithMockito
@WithElasticClient.All
class StorageCommandsTest {

    private static final VersionCommand VERSION = VersionCommand.builder().build();
    private static final HealthCheckCommand HEALTH = HealthCheckCommand.builder().build();

    @Nested
    class SuccessTest {

        @Test
        @DisplayName("Version")
        void testVersionCommand(final StorageClient storage) {
            assertEquals(
                    ElasticContainerExtension.DEFAULT_ELASTIC_SEARCH_VERSION,
                    VERSION.execute(storage),
                    "version must match");
        }

        @Test
        @DisplayName("Health")
        void testHealthCommand(final StorageClient storage) {
            assertNotNull(HEALTH.execute(storage), "status must be valid");
        }
    }

    @Nested
    class FailureTest {

        @Mock(answer = Answers.RETURNS_DEEP_STUBS)
        private ColumbusElasticClient storage;

        @Test
        @DisplayName("Version")
        void testVersionCommand() throws IOException {
            when(storage.client().info()).thenThrow(IOException.class);

            final var handler = new VersionHandler<ColumbusElasticClient>();

            assertThrows(
                    IOException.class,
                    () -> handler.handle(VERSION, storage),
                    "error must be propagated");
        }

        @Test
        @DisplayName("Health")
        void testHealthCommand() throws IOException {
            when(storage.client().cluster().health(any(HealthRequest.class))).thenThrow(IOException.class);

            final var handler = new HealthCheckHandler<ColumbusElasticClient>();

            assertEquals(HealthStatus.UNHEALTHY, handler.handle(HEALTH, storage), "status must be unhealthy");
        }
    }
}
