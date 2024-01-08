package com.phonepe.commons.columbus.internal.registry;

import com.phonepe.commons.columbus.registry.Registries;
import com.phonepe.commons.columbus.registry.Registry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmptyRegistryTest {

    @Test
    @DisplayName("always returns no values")
    void testGet() {
        final Registry<Void, String> registry = Registries.empty();

        assertTrue(registry.get(null).isEmpty());
        assertInstanceOf(EmptyRegistry.class, registry, "registry should be empty");
    }
}
