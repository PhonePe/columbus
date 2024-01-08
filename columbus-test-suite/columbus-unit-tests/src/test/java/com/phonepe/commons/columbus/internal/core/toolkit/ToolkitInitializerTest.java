package com.phonepe.commons.columbus.internal.core.toolkit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.commons.columbus.core.toolkit.ToolkitHelpers;
import com.phonepe.commons.columbus.core.toolkit.ToolkitSettings;
import com.phonepe.commons.columbus.registry.Registries;
import com.phonepe.commons.columbus.test.annotations.WithMapper;
import com.phonepe.commons.columbus.type.TypeRegistry;
import com.phonepe.commons.columbus.type.Types;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WithMapper
class ToolkitInitializerTest {

    private static final TypeRegistry typeRegistry = Types.typeRegistry(Registries.empty());
    private static final ToolkitSettings settings = ToolkitSettings.defaults();

    @Test
    @DisplayName("initialize")
    void testInitialize(final ObjectMapper mapper) {
        final var initializer = ToolkitHelpers.initializer();
        initializer.use(typeRegistry);
        initializer.use(mapper);
        assertThrows(IllegalStateException.class, initializer::get, "cannot initialize toolkit");
        initializer.use(settings);

        final var toolkit = initializer.get();

        assertAll(
                "toolkit",
                () -> assertNotNull(toolkit, "must return a valid toolkit"),
                () -> assertNotNull(toolkit.reflect(), "must have a valid reflect"),
                () -> assertNotNull(toolkit.serDe(), "must have a valid mapper"),
                () -> assertNotNull(toolkit.registryFactory(), "must have a valid registry factory"),
                () -> assertNotNull(toolkit.visitorFactory(), "must have a valid visitor factory"),
                () -> assertNotNull(toolkit.handlerFactory(), "must have a valid handler factory"));
    }

    @Test
    @DisplayName("initialize without type registry")
    void testInitializeWithoutTypeRegistry(final ObjectMapper mapper) {
        final var initializer = ToolkitHelpers.initializer();
        initializer.use(mapper);
        assertThrows(IllegalStateException.class, initializer::get, "cannot initialize toolkit");
        initializer.use(settings);

        final var toolkit = initializer.get();

        assertAll(
                "toolkit",
                () -> assertNotNull(toolkit, "must return a valid toolkit"),
                () -> assertNotNull(toolkit.reflect(), "must have a valid reflect"),
                () -> assertNotNull(toolkit.serDe(), "must have a valid mapper"),
                () -> assertNotNull(toolkit.registryFactory(), "must have a valid registry factory"),
                () -> assertNotNull(toolkit.visitorFactory(), "must have a valid visitor factory"),
                () -> assertNotNull(toolkit.handlerFactory(), "must have a valid handler factory"));
    }

    @Test
    @DisplayName("initialize with null type registry")
    void testInitializeWithNullTypeRegistry(final ObjectMapper mapper) {
        final var initializer = ToolkitHelpers.initializer();
        initializer.use((TypeRegistry) null);
        initializer.use(mapper);
        assertThrows(IllegalStateException.class, initializer::get, "cannot initialize toolkit");
        initializer.use(settings);

        final var toolkit = initializer.get();

        assertAll(
                "toolkit",
                () -> assertNotNull(toolkit, "must return a valid toolkit"),
                () -> assertNotNull(toolkit.reflect(), "must have a valid reflect"),
                () -> assertNotNull(toolkit.serDe(), "must have a valid mapper"),
                () -> assertNotNull(toolkit.registryFactory(), "must have a valid registry factory"),
                () -> assertNotNull(toolkit.visitorFactory(), "must have a valid visitor factory"),
                () -> assertNotNull(toolkit.handlerFactory(), "must have a valid handler factory"));
    }
}
