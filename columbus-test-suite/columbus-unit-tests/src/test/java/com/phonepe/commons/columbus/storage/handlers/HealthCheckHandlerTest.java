package com.phonepe.commons.columbus.storage.handlers;

import com.phonepe.commons.columbus.storage.commands.HealthCheckCommand;
import com.phonepe.commons.columbus.storage.commands.HealthCheckCommand.HealthStatus;
import com.phonepe.commons.columbus.test.annotations.WithMemoryStorage;
import com.phonepe.commons.columbus.test.storage.memory.MemoryStorage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WithMemoryStorage.All
class HealthCheckHandlerTest {

    @Test
    void testCommand(final MemoryStorage storage) {
        final var command = HealthCheckCommand.builder().build();

        assertEquals(HealthStatus.HEALTHY, command.execute(storage), "health status should match");
    }
}
