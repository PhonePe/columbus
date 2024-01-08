package com.phonepe.commons.columbus.internal.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.phonepe.commons.columbus.registry.MutableRegistry;
import com.phonepe.commons.columbus.registry.Registries;
import com.phonepe.commons.columbus.type.JavaType;
import com.phonepe.commons.columbus.type.NamedType;
import com.phonepe.commons.columbus.type.TypeResolver;
import com.phonepe.commons.columbus.type.Types;
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
