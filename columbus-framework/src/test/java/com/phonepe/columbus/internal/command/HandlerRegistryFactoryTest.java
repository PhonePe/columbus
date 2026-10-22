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
import com.phonepe.columbus.command.factory.HandlerRegistryFactory;
import com.phonepe.columbus.internal.command.Commands.Copy;
import com.phonepe.columbus.internal.command.Commands.Network;
import com.phonepe.columbus.internal.command.Commands.NetworkHandler;
import com.phonepe.columbus.internal.command.Commands.Save;
import com.phonepe.columbus.internal.command.Commands.Storage;
import com.phonepe.columbus.internal.command.Commands.StorageHandler;
import com.phonepe.columbus.reflect.Generics;
import com.phonepe.columbus.reflect.Reflect;
import com.phonepe.columbus.reflect.ReflectHelpers;
import com.phonepe.columbus.registry.Registries;
import com.phonepe.columbus.registry.RegistryHelpers;
import com.phonepe.columbus.registry.factory.RegistryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HandlerRegistryFactoryTest {

    private static final Save saveCommand = Save.builder()
            .contents("contents")
            .build();
    private static final Copy copyCommand = Copy.builder().build();

    private static final Reflect reflect = ReflectHelpers.defaultInstance();
    private static final RegistryFactory registryFactory = RegistryHelpers.factory(reflect);
    private static final HandlerRegistryFactory factory = CommandHelpers.factory(registryFactory);

    @Nested
    class NameHandlerTest {

        @Test
        @DisplayName("create handler registry")
        void testCreateHandlerRegistry() {
            final var options = CommandHelpers.optionsForName(
                            Generics.typed(StorageHandler.class),
                            Storage.class)
                    .build();
            final var nameRegistry = factory.create(options);
            final var registry = Registries.transformingKey(
                    command -> command.getClass().getSimpleName(),
                    nameRegistry);
            final var resolver = CommandHelpers.resolver(registry);
            final var value = new AtomicReference<String>();
            final var executor = Storage.builder()
                    .resolver(resolver)
                    .saver(value::set)
                    .build();

            assertAll(
                    "commands",
                    () -> assertAll(
                            "save command",
                            () -> assertTrue(executor.execute(saveCommand), "save should return true"),
                            () -> assertEquals("contents", value.get(), "save should call saver")),
                    () -> assertThrows(
                            IllegalArgumentException.class,
                            () -> executor.execute(copyCommand),
                            "no handler for copy"));
        }

        @Test
        @DisplayName("create empty handler registry")
        void testCreateEmptyHandlerRegistry() {
            final var options = CommandHelpers.optionsForName(
                            Generics.typed(NetworkHandler.class),
                            Network.class)
                    .build();
            final var nameRegistry = factory.create(options);
            final var registry = Registries.transformingKey(
                    command -> command.getClass().getSimpleName(),
                    nameRegistry);
            final var resolver = CommandHelpers.resolver(registry);
            final var value = new AtomicReference<String>();
            final var executor = Network.builder()
                    .resolver(resolver)
                    .sender(value::set)
                    .build();

            assertAll(
                    "commands",
                    () -> assertThrows(
                            IllegalArgumentException.class,
                            () -> executor.execute(saveCommand),
                            "no handler for save"),
                    () -> assertThrows(
                            IllegalArgumentException.class,
                            () -> executor.execute(copyCommand),
                            "no handler for copy"));
        }
    }

    @Nested
    class TypeHandlerTest {

        @Test
        @DisplayName("create handler registry")
        void testCreateHandlerRegistry() {
            final var options = CommandHelpers.optionsForType(
                            Generics.typed(StorageHandler.class),
                            Storage.class)
                    .build();
            final var nameRegistry = factory.create(options);
            final var registry = Registries.usingClassHierarchy(nameRegistry, Command.class);
            final var resolver = CommandHelpers.resolver(registry);
            final var value = new AtomicReference<String>();
            final var executor = Storage.builder()
                    .resolver(resolver)
                    .saver(value::set)
                    .build();

            assertAll(
                    "commands",
                    () -> assertAll(
                            "save command",
                            () -> assertTrue(executor.execute(saveCommand), "save should return true"),
                            () -> assertEquals("contents", value.get(), "save should call saver")),
                    () -> assertThrows(
                            IllegalArgumentException.class,
                            () -> executor.execute(copyCommand),
                            "no handler for copy"));
        }
    }
}
