package com.phonepe.columbus.internal.core.toolkit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.columbus.core.toolkit.ToolkitHelpers;
import com.phonepe.columbus.core.toolkit.ToolkitSettings;
import com.phonepe.columbus.registry.Registries;
import com.phonepe.columbus.test.annotations.WithMapper;
import com.phonepe.columbus.type.TypeRegistry;
import com.phonepe.columbus.type.Types;
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
}
