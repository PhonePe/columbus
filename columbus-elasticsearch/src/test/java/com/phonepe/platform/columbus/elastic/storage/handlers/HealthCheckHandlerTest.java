package com.phonepe.platform.columbus.elastic.storage.handlers;

import com.phonepe.platform.columbus.elastic.extensions.client.WithElasticClient;
import com.phonepe.platform.columbus.elastic.internal.storage.ColumbusElasticClient;
import com.phonepe.platform.columbus.storage.StorageClient;
import com.phonepe.platform.columbus.storage.commands.HealthCheckCommand;
import com.phonepe.platform.columbus.storage.commands.HealthCheckCommand.HealthStatus;
import com.phonepe.platform.columbus.test.extensions.WithMockito;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WithMockito
@WithElasticClient.All
class HealthCheckHandlerTest {

    private static final HealthCheckCommand HEALTH = HealthCheckCommand.builder().build();

    @Nested
    class SuccessTest {

        @Test
        void testCommand(final StorageClient storage) {
            assertEquals(HealthStatus.HEALTHY, HEALTH.execute(storage), "status must be healthy");
        }
    }

    @Nested
    class FailureTest {

        @Mock(answer = Answers.RETURNS_DEEP_STUBS)
        private ColumbusElasticClient storage;

        @Test
        void testCommand() throws IOException {
            when(storage.client().cluster().health(any(), any())).thenThrow(IOException.class);

            final var handler = new HealthCheckHandler<ColumbusElasticClient>();

            assertEquals(HealthStatus.UNHEALTHY, handler.handle(HEALTH, storage), "status must be unhealthy");
        }
    }
}
