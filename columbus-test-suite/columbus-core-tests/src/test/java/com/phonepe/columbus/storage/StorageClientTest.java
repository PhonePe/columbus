/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phonepe.columbus.storage;

import com.phonepe.columbus.command.Command;
import com.phonepe.columbus.command.CommandHelpers;
import com.phonepe.columbus.registry.Registries;
import com.phonepe.columbus.storage.commands.VersionCommand;
import com.phonepe.columbus.test.annotations.WithMemoryStorage;
import com.phonepe.columbus.test.annotations.WithToolkit;
import com.phonepe.columbus.test.storage.memory.MemoryStorage;
import com.phonepe.columbus.test.storage.memory.MemoryStorageConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WithToolkit.All
class StorageClientTest {

    private static final VersionCommand COMMAND = VersionCommand.builder().build();

    @Nested
    @WithMemoryStorage.All
    class MemoryStorageHandlersTest {

        @Test
        @DisplayName("version command returns storage version")
        void testVersionCommand(final StorageClient storage) {
            assertEquals(MemoryStorage.VERSION, COMMAND.execute(storage), "version should match");
            assertEquals(VersionCommand.COMMAND_NAME, COMMAND.name(), "name should match");
        }

        @Test
        @DisplayName("unsupported command")
        void testUnsupportedCommand(final StorageClient storage) {
            final var command = new InvalidCommand();
            assertThrows(
                    UnsupportedOperationException.class,
                    () -> command.execute(storage),
                    "no handler registered for the command");
        }
    }

    @Nested
    class MemoryStorageClientTest {

        private static final MemoryStorageConfiguration configuration = MemoryStorageConfiguration.builder()
                .enabled(true)
                .name("memory-test")
                .build();

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

        @Test
        @DisplayName("missing command handler")
        void testMissingCommand() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> COMMAND.execute(storage),
                    "no handler registered for the command");
        }
    }

    private static final class InvalidCommand implements Command<String> {

        @Override
        public String name() {
            return "INVALID";
        }
    }
}
