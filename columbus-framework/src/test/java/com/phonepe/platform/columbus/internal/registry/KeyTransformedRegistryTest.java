package com.phonepe.platform.columbus.internal.registry;

import com.phonepe.platform.columbus.registry.MutableRegistry;
import com.phonepe.platform.columbus.registry.Registries;
import com.phonepe.platform.columbus.registry.Registry;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KeyTransformedRegistryTest {

    private static final MutableRegistry<String, Integer> delegate = Registries.stored();
    private static final Registry<String, Integer> registry = Registries.transformingKey(
            Transforms::keyTransform,
            delegate
    );

    @Test
    @DisplayName("successful transforms")
    void testSuccessfulTransform() {
        assertEquals(10, registry.get("Key1").orElse(null));
        assertEquals(10, registry.get("KEY1").orElse(null));
        assertEquals(20, registry.get("key2").orElse(null));
        assertEquals(-30, registry.get("key3").orElse(null));
    }

    @Test
    @DisplayName("missing value")
    void testMissingValue() {
        assertTrue(registry.get("missing").isEmpty());
    }

    @Test
    @DisplayName("key rejected")
    void testKeyReject() {
        assertTrue(registry.get("a very long key").isEmpty());
        assertTrue(registry.get(null).isEmpty());
    }

    @BeforeAll
    static void beforeAll() {
        delegate.register("key1", 10);
        delegate.register("key2", 20);
        delegate.register("key3", -30);
    }
}
