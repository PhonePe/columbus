package com.phonepe.platform.columbus.internal.registry;

import com.phonepe.platform.columbus.registry.MutableRegistry;
import com.phonepe.platform.columbus.registry.Registries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StoredRegistryTest {

    private static final String KEY_INITIAL = "key-initial";
    private static final String VALUE_INITIAL = "value-initial";
    private static final String KEY_STORED = "key-stored";
    private static final String VALUE_STORED = "value-stored";
    private static final String KEY_MISSING = "key-missing";

    private static final Map<String, String> initialMap = Map.of(KEY_INITIAL, VALUE_INITIAL);

    @Nested
    @DisplayName("Mutable store")
    @TestMethodOrder(OrderAnnotation.class)
    class MutableStoredRegistryTest {

        private static final MutableRegistry<String, String> registry = Registries.stored(new HashMap<>(initialMap));

        @Test
        @Order(1)
        @DisplayName("retains keys provided on init")
        void testInitialEntryPresent() {
            assertEquals(VALUE_INITIAL, registry.get(KEY_INITIAL).orElse(null));
        }

        @Test
        @Order(2)
        @DisplayName("new values are returned on next fetches")
        void testPutAndGet() {
            assertTrue(registry.get(KEY_STORED).isEmpty());
            registry.register(KEY_STORED, VALUE_STORED);
            assertEquals(VALUE_STORED, registry.get(KEY_STORED).orElse(null));
        }

        @Test
        @Order(3)
        @DisplayName("missing key returns empty")
        void testEmptyWhenMissing() {
            assertTrue(registry.get(KEY_MISSING).isEmpty());
        }

        @Test
        @Order(4)
        @DisplayName("values can be overwritten")
        void testOverwrite() {
            assertEquals(VALUE_STORED, registry.get(KEY_STORED).orElse(null));
            registry.register(KEY_STORED, VALUE_INITIAL);
            assertEquals(VALUE_INITIAL, registry.get(KEY_STORED).orElse(null));
        }
    }

    @Nested
    @DisplayName("Immutable store")
    class ImmutableStoredRegistryTest {

        private static final MutableRegistry<String, String> registry = Registries.stored(initialMap);

        @Test
        @DisplayName("retains keys provided on init")
        void testInitialEntryPresent() {
            assertEquals(VALUE_INITIAL, registry.get(KEY_INITIAL).orElse(null));
        }

        @Test
        @DisplayName("missing key returns empty")
        void testEmptyWhenMissing() {
            assertTrue(registry.get(KEY_MISSING).isEmpty());
        }

        @Test
        @DisplayName("values cannot be overwritten")
        void testOverwrite() {
            assertTrue(registry.get(KEY_STORED).isEmpty());
            assertThrows(UnsupportedOperationException.class, () -> registry.register(KEY_STORED, VALUE_STORED));
            assertTrue(registry.get(KEY_STORED).isEmpty());
        }
    }
}
