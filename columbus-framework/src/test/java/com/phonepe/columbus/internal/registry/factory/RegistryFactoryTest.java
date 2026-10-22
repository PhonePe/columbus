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
package com.phonepe.columbus.internal.registry.factory;

import com.phonepe.columbus.reflect.Reflect;
import com.phonepe.columbus.reflect.ReflectHelpers;
import com.phonepe.columbus.registry.RegistryHelpers;
import com.phonepe.columbus.registry.factory.RegistryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegistryFactoryTest {

    private static final Reflect reflect = ReflectHelpers.defaultInstance();

    private static final RegistryFactory factory = RegistryHelpers.factory(reflect);

    @Nested
    class NameRegistryTest {

        @Test
        @DisplayName("create registry with no conflicts")
        void testCreateRegistry() {
            final var options = RegistryHelpers.optionsForName(BaseType.SubType.class)
                    .build();
            final var registry = factory.create(options);
            assertAll(
                    "registry entries",
                    () -> assertInstanceOf(
                            BaseType.TypeA2.class,
                            registry.get("A").orElse(null),
                            "registry has instance of subtype"),
                    () -> assertInstanceOf(
                            BaseType.TypeB2.class,
                            registry.get("B").orElse(null),
                            "registry has instance of subtype")
            );
        }

        @Test
        @DisplayName("create registry with conflicts")
        void testCreateRegistryWithConflicts() {
            final var options = RegistryHelpers.optionsForName(BaseType.class)
                    .build();
            assertThrows(
                    IllegalArgumentException.class,
                    () -> factory.create(options),
                    "conflicting entries for registry should throw");
        }

        @Test
        @DisplayName("create registry ignoring conflicts")
        void testCreateRegistryIgnoringConflicts() {
            final var options = RegistryHelpers.optionsForName(BaseType.class)
                    .ignorePriorityConflicts(true)
                    .build();
            final var registry = factory.create(options);
            final var typeA = registry.get("A").orElse(null);
            assertAll(
                    "registry entries",
                    () -> assertTrue(
                            (typeA instanceof BaseType.TypeA) || (typeA instanceof BaseType.TypeA2),
                            "registry chooses one of the valid types with highest priority"),
                    () -> assertInstanceOf(
                            BaseType.TypeB.class,
                            registry.get("B").orElse(null),
                            "registry has instance with higher priority")
            );
        }
    }

    @Nested
    class TypeRegistryTest {

        @Test
        @DisplayName("create registry with multiple annotations and priority")
        void testCreateRegistry() {
            final var options = RegistryHelpers.optionsForType(BaseType.class)
                    .build();
            final var registry = factory.create(options);
            assertAll(
                    "registry entries",
                    () -> assertInstanceOf(
                            BaseType.TypeA.class,
                            registry.get(BaseType.class).orElse(null),
                            "registry has instance of subtype"),
                    () -> assertInstanceOf(
                            BaseType.TypeA2.class,
                            registry.get(BaseType.SubType.class).orElse(null),
                            "registry has instance of subtype")
            );
        }

        @Test
        @DisplayName("no valid annotations")
        void testCreateEmptyRegistry() {
            final var options = RegistryHelpers.optionsForType(BaseType.TypeB2.class)
                    .build();
            final var registry = factory.create(options);
            assertAll(
                    "registry has no entries",
                    () -> assertTrue(registry.get(BaseType.class).isEmpty()),
                    () -> assertTrue(registry.get(BaseType.SubType.class).isEmpty()),
                    () -> assertTrue(registry.get(BaseType.TypeA.class).isEmpty()),
                    () -> assertTrue(registry.get(BaseType.TypeB.class).isEmpty()),
                    () -> assertTrue(registry.get(BaseType.TypeA2.class).isEmpty()),
                    () -> assertTrue(registry.get(BaseType.TypeB2.class).isEmpty())
            );
        }
    }
}
