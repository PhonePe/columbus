package com.phonepe.platform.columbus.internal.registry;

import com.phonepe.platform.columbus.registry.MutableRegistry;
import com.phonepe.platform.columbus.registry.Registries;
import com.phonepe.platform.columbus.registry.Registry;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChainedRegistryTest {

    private static final MutableRegistry<String, Integer> registry1 = Registries.stored();
    private static final MutableRegistry<String, Integer> registry2 = Registries.stored();
    private static final Registry<String, Integer> registry = Registries.chaining(registry1)
            .add(registry2)
            .build();

    @Test
    @DisplayName("fetch from first")
    void testFetchFromFirst() {
        assertEquals(10, registry.get("key1").orElse(null));
    }

    @Test
    @DisplayName("fetch from second")
    void testFetchFromSecond() {
        assertEquals(20, registry.get("key2").orElse(null));
    }

    @Test
    @DisplayName("missing value")
    void testMissingValue() {
        assertTrue(registry.get("missing").isEmpty());
    }

    @BeforeAll
    static void beforeAll() {
        registry1.register("key1", 10);
        registry2.register("key1", 30);
        registry2.register("key2", 20);
    }
}
