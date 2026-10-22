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
