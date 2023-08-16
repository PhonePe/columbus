package com.phonepe.platform.columbus.storage.handlers;

import com.phonepe.platform.columbus.extensions.memory.WithMemoryStorage;
import com.phonepe.platform.columbus.storage.commands.HealthCheckCommand;
import com.phonepe.platform.columbus.storage.commands.HealthCheckCommand.HealthStatus;
import com.phonepe.platform.columbus.storage.memory.MemoryStorage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WithMemoryStorage.All
class HealthCheckHandlerTest {

    @Test
    void testCommand(MemoryStorage storage) {
        final var command = HealthCheckCommand.builder().build();

        assertEquals(HealthStatus.HEALTHY, command.execute(storage), "health status should match");
    }
}
