package com.phonepe.platform.columbus.internal.registry;

import com.phonepe.platform.columbus.registry.MutableRegistry;
import com.phonepe.platform.columbus.registry.Registries;
import com.phonepe.platform.columbus.registry.Registry;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HierarchicalKeyRegistryTest {

    private static final MutableRegistry<String, Integer> delegate = Registries.stored();
    private static final Registry<String, Integer> registry = Registries.hierarchicalKey(
            delegate,
            Transforms::hierarchicalKeyTransform
    );

    @Test
    @DisplayName("exact key match")
    void testExactKey() {
        assertEquals(10, registry.get("key").orElse(null));
        assertEquals(20, registry.get("key-long").orElse(null));
    }

    @Test
    @DisplayName("missing value")
    void testMissingValue() {
        assertTrue(registry.get("missing").isEmpty());
        assertTrue(registry.get("ke").isEmpty());
        assertTrue(registry.get("keg").isEmpty());
    }

    @Test
    @DisplayName("key transformed")
    void testKeyTransformed() {
        assertEquals(10, registry.get("key-short").orElse(null));
        assertEquals(20, registry.get("key-longest").orElse(null));
    }

    @BeforeAll
    static void beforeAll() {
        delegate.register("key", 10);
        delegate.register("key-long", 20);
    }
}
