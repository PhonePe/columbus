package com.phonepe.columbus.internal.command;

import com.phonepe.columbus.command.Command;
import com.phonepe.columbus.command.CommandHelpers;
import com.phonepe.columbus.command.Executor;
import com.phonepe.columbus.command.impl.CommandHandler;
import com.phonepe.columbus.command.impl.HandlerResolver;
import com.phonepe.columbus.internal.command.Commands.Copy;
import com.phonepe.columbus.internal.command.Commands.Save;
import com.phonepe.columbus.internal.command.Commands.SaveHandler;
import com.phonepe.columbus.internal.command.Commands.Storage;
import com.phonepe.columbus.registry.Registries;
import com.phonepe.columbus.registry.Registry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExecutorTest {

    private static final Registry<Command<?>, CommandHandler<?, ? extends Command<?>, Storage>> registry =
            Registries.transformingKey(
                    Object::getClass,
                    Registries.stored(new HashMap<>(Map.of(Save.class, new SaveHandler()))));

    private static final HandlerResolver<Storage> resolver = CommandHelpers.resolver(registry);

    private static final Executor executor = Storage.builder()
            .resolver(resolver)
            .saver(string -> {})
            .build();

    @Test
    @DisplayName("execute command")
    void testExecuteCommand() {
        final var result = Save.builder()
                .build()
                .execute(executor);
        assertTrue(result, "execution result should match");
    }

    @Test
    @DisplayName("command is not registered")
    void testExecuteCommandMissing() {
        final var command = Copy.builder().build();
        assertThrows(IllegalArgumentException.class, () -> command.execute(executor));
    }
}
