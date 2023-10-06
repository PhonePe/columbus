package com.phonepe.platform.columbus.storage.handlers;

import com.phonepe.platform.columbus.storage.commands.VersionCommand;
import com.phonepe.platform.columbus.test.annotations.WithMemoryStorage;
import com.phonepe.platform.columbus.test.storage.memory.MemoryStorage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WithMemoryStorage.All
class VersionHandlerTest {

    @Test
    void testCommand(MemoryStorage storage) {
        final var command = VersionCommand.builder().build();

        assertEquals(MemoryStorage.VERSION, command.execute(storage), "version should match");
    }
}
