package com.phonepe.platform.columbus.internal.core.toolkit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.platform.columbus.core.toolkit.ToolkitHelpers;
import com.phonepe.platform.columbus.core.toolkit.ToolkitSettings;
import com.phonepe.platform.columbus.registry.Registries;
import com.phonepe.platform.columbus.type.TypeRegistry;
import com.phonepe.platform.columbus.type.Types;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ToolkitInitializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final TypeRegistry typeRegistry = Types.typeRegistry(Registries.empty());
    private static final ToolkitSettings settings = ToolkitSettings.defaults();

    @Test
    @DisplayName("initialize")
    void testInitialize() {
        final var initializer = ToolkitHelpers.initializer();
        initializer.use(typeRegistry);
        initializer.use(mapper);
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
    void testInitializeWithoutTypeRegistry() {
        final var initializer = ToolkitHelpers.initializer();
        initializer.use(mapper);
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
