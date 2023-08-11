package com.phonepe.platform.columbus.storage;

import com.phonepe.platform.columbus.command.Command;
import com.phonepe.platform.columbus.command.CommandHelpers;
import com.phonepe.platform.columbus.reflect.Generics;
import com.phonepe.platform.columbus.reflect.ReflectHelpers;
import com.phonepe.platform.columbus.registry.Registries;
import com.phonepe.platform.columbus.registry.RegistryHelpers;
import com.phonepe.platform.columbus.storage.commands.VersionCommand;
import com.phonepe.platform.columbus.storage.memory.MemoryStorage;
import com.phonepe.platform.columbus.storage.memory.MemoryStorageConfiguration;
import com.phonepe.platform.columbus.storage.memory.handlers.MemoryStorageCommandHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StorageClientTest {

    private static final MemoryStorageConfiguration configuration = MemoryStorageConfiguration.builder()
            .enabled(true)
            .name("memory-test")
            .build();

    @Nested
    class MemoryStorageHandlersTest {

        private static final StorageClient storage;

        @Test
        @DisplayName("version command returns storage version")
        void testVersionCommand() {
            final var command = VersionCommand.builder().build();
            assertEquals(MemoryStorage.VERSION, command.execute(storage), "version should match");
            assertEquals(VersionCommand.COMMAND_NAME, command.name(), "name should match");
        }

        @Test
        @DisplayName("missing command handler")
        void testMissingCommand() {
            final var command = new Command<String>() {
                @Override
                public String name() {
                    return "DUMMY";
                }
            };
            assertThrows(
                    IllegalArgumentException.class,
                    () -> command.execute(storage),
                    "no handler registered for the command");
        }

        static {
            final var factory = CommandHelpers.factory(RegistryHelpers.factory(ReflectHelpers.defaultInstance()));
            final var options = CommandHelpers.optionsForType(
                            Generics.typed(MemoryStorageCommandHandler.class),
                            MemoryStorage.class)
                    .build();
            final var registry = Registries.usingClassHierarchy(factory.create(options), Command.class);
            storage = MemoryStorage.builder()
                    .resolver(CommandHelpers.resolver(registry))
                    .configuration(configuration)
                    .build();
        }
    }

    @Nested
    class MemoryStorageUnsupportedHandlersTest {

        private static final StorageClient storage;

        @Test
        @DisplayName("missing command handler")
        void testMissingCommand() {
            final var command = new Command<String>() {
                @Override
                public String name() {
                    return "DUMMY";
                }
            };
            assertThrows(
                    UnsupportedOperationException.class,
                    () -> command.execute(storage),
                    "no handler registered for the command");
        }

        static {
            final var factory = CommandHelpers.factory(RegistryHelpers.factory(ReflectHelpers.defaultInstance()));
            final var options = CommandHelpers.optionsForType(
                            Generics.typed(StorageCommandHandler.class),
                            MemoryStorage.class)
                    .build();
            final var registry = Registries.usingClassHierarchy(factory.create(options), Command.class);
            storage = MemoryStorage.builder()
                    .resolver(CommandHelpers.resolver(registry))
                    .configuration(configuration)
                    .build();
        }
    }

    @Nested
    class MemoryStorageClientTest {

        private static final StorageClient storage = MemoryStorage.builder()
                .resolver(CommandHelpers.resolver(Registries.empty()))
                .configuration(configuration)
                .build();

        @Test
        @DisplayName("storage type matches configuration")
        void testType() {
            assertEquals(configuration.getType(), storage.type(), "storage type should be same as configuration");
        }

        @Test
        @DisplayName("storage name matches configuration")
        void testName() {
            assertEquals(configuration.getName(), storage.name(), "storage name should be same as configuration");
        }
    }
}
