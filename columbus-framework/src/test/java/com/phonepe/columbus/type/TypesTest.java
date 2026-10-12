package com.phonepe.columbus.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.phonepe.columbus.registry.Registries;
import com.phonepe.columbus.type.EntityType.Visitor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class TypesTest {

    public static final Visitor<List<Integer>, String, Void> NAME_EXTRACTOR = new Visitor<>() {

        @Override
        public String visit(final NamedType<List<Integer>> type, final Void context) {
            return type.getName();
        }

        @Override
        public String visit(final JavaType<List<Integer>> type, final Void context) {
            return type.getType().getType().getTypeName();
        }
    };

    @Test
    @DisplayName("named type uses same name")
    void testNamedType() {
        final var type = Types.<String>forName("string");

        assertEquals("string", type.getName(), "named types use the provided name");
        assertEquals("string", type.name(), "named types use the same name for representation");
    }

    @Test
    @DisplayName("using a class")
    void testClass() {
        final var type = Types.forClass(String.class);

        assertEquals("java.lang.String", type.name(), "java types use fully qualified name");
    }

    @Test
    @DisplayName("type reference captures complex types")
    void testTypeReference() {
        final var type = Types.forTypeReference(new TypeReference<List<String>>() {});

        assertEquals("java.util.List<java.lang.String>", type.name(), "java types use fully qualified name");
    }

    @Test
    @DisplayName("create type registry")
    void testTypeRegistry() {
        final var registry = Registries.<String, JavaType<?>>empty();
        final var typeRegistry = Types.typeRegistry(registry);
        final var wrappedTypeRegistry = Types.typeRegistry(typeRegistry);

        assertSame(typeRegistry, wrappedTypeRegistry, "type registry is not wrapped by another");
    }

    @Nested
    class VisitorTest {

        @Test
        @DisplayName("named type returns provided name")
        void testNamedType() {
            final var type = Types.<List<Integer>>forName("integers");

            assertEquals(
                    "integers",
                    type.accept(NAME_EXTRACTOR, null),
                    "named types retain the name");
        }

        @Test
        @DisplayName("java type returns type name")
        void testJavaType() {
            final var type = Types.forTypeReference(new TypeReference<List<Integer>>() {});

            assertEquals(
                    "java.util.List<java.lang.Integer>",
                    type.accept(NAME_EXTRACTOR, null),
                    "named types retain the name");
        }
    }
}
