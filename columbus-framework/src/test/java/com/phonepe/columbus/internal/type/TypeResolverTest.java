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
package com.phonepe.columbus.internal.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.phonepe.columbus.registry.MutableRegistry;
import com.phonepe.columbus.registry.Registries;
import com.phonepe.columbus.type.JavaType;
import com.phonepe.columbus.type.NamedType;
import com.phonepe.columbus.type.TypeResolver;
import com.phonepe.columbus.type.Types;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TypeResolverTest {

    private static final JavaType<String> classType = Types.forClass(String.class);
    private static final JavaType<List<String>> jacksonType = Types.forTypeReference(new TypeReference<>() {});
    private static final NamedType<String> namedType = Types.forName("string");

    private static final MutableRegistry<String, JavaType<?>> typeRegistry = Registries.stored();
    private static final TypeResolver resolver = Types.resolver(Types.typeRegistry(typeRegistry));

    @BeforeAll
    static void beforeAll() {
        typeRegistry.register(namedType.getName(), classType);
        typeRegistry.register("strings", jacksonType);
    }

    @Nested
    class TypeResolveSuccessfulTest {

        @Test
        @DisplayName("resolving java type")
        void testResolveNoOp() {
            assertAll(
                    "java types must be returned as is",
                    () -> assertEquals(
                            classType,
                            resolver.toJavaType(classType),
                            "class types must be returned as is"),
                    () -> assertEquals(
                            jacksonType,
                            resolver.toJavaType(jacksonType),
                            "type references must be returned as is")
            );
        }

        @Test
        @DisplayName("resolving registered named type")
        void testResolveOne() {
            assertAll(
                    "java types must be fetched from registry",
                    () -> assertEquals(
                            classType,
                            resolver.toJavaType(Types.forName("string")),
                            "class types must be fetched from registry"),
                    () -> assertEquals(
                            jacksonType,
                            resolver.toJavaType(Types.forName("strings")),
                            "type references must be fetched from registry")
            );
        }
    }

    @Nested
    class TypeResolveFailureTest {

        @Test
        @DisplayName("resolving unregistered named type")
        void testResolveFailedMissing() {
            final var missingType = Types.forName("missing");

            assertThrows(
                    IllegalArgumentException.class,
                    () -> resolver.toJavaType(missingType),
                    "missing types in registry should throw");
        }
    }
}
