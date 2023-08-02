package com.phonepe.platform.columbus.internal.registry;

import com.phonepe.platform.columbus.registry.MutableRegistry;
import com.phonepe.platform.columbus.registry.Registries;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;


class MutableCachedRegistryTest {

    public static final String KEY = "key";
    public static final String VALUE = "value";
    public static final String NEW_KEY = "new";
    public static final String NEW_VALUE = "new-value";

    @Nested
    @TestMethodOrder(OrderAnnotation.class)
    class CacheTest {

        private static final MutableRegistry<String, String> delegate = mock();
        private static final MutableRegistry<String, String> registry = Registries.mutableCached(delegate);

        @Test
        @Order(1)
        @DisplayName("cache miss calls delegate")
        void testCacheMiss() {
            assertEquals(VALUE, registry.get(KEY).orElse(null));
            verify(delegate, times(1)).get(KEY);
            verifyNoMoreInteractions(delegate);
        }

        @Test
        @Order(2)
        @DisplayName("cache hit does not call delegate")
        void testCacheHit() {
            assertEquals(VALUE, registry.get(KEY).orElse(null));
            verifyNoInteractions(delegate);
        }

        @Test
        @Order(3)
        @DisplayName("register calls delegate")
        void testCacheWrite() {
            registry.register(NEW_KEY, NEW_VALUE);
            verify(delegate, times(1)).register(NEW_KEY, NEW_VALUE);
            verifyNoMoreInteractions(delegate);
        }

        @BeforeEach
        void setUp() {
            doReturn(Optional.of(VALUE)).when(delegate).get(KEY);
        }

        @AfterEach
        @SuppressWarnings("unchecked")
        void tearDown() {
            reset(delegate);
        }
    }

    @Nested
    @TestMethodOrder(OrderAnnotation.class)
    class StorageTest {

        private static final MutableRegistry<String, String> delegate = Registries.stored();
        private static final MutableRegistry<String, String> registry = Registries.mutableCached(delegate);

        @Test
        @Order(1)
        @DisplayName("empty registry returns nothing")
        void testNoEntry() {
            assertTrue(registry.get(KEY).isEmpty());
        }

        @Test
        @Order(2)
        @DisplayName("write updates cache")
        void testWriteUpdatesCache() {
            registry.register(KEY, VALUE);
            assertEquals(VALUE, registry.get(KEY).orElse(null));
        }

        @Test
        @Order(3)
        @DisplayName("write updates delegate")
        void testWriteUpdatesDelegate() {
            assertEquals(VALUE, delegate.get(KEY).orElse(null));
        }
    }
}
