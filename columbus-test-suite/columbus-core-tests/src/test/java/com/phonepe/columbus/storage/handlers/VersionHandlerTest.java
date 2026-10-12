package com.phonepe.columbus.storage.handlers;

import com.phonepe.columbus.storage.commands.VersionCommand;
import com.phonepe.columbus.test.annotations.WithMemoryStorage;
import com.phonepe.columbus.test.storage.memory.MemoryStorage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WithMemoryStorage.All
class VersionHandlerTest {

    @Test
    void testCommand(final MemoryStorage storage) {
        final var command = VersionCommand.builder().build();

        assertEquals(MemoryStorage.VERSION, command.execute(storage), "version should match");
    }
}
