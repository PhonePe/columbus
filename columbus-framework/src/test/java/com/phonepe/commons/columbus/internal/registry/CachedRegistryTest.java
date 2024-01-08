package com.phonepe.commons.columbus.internal.registry;

import com.phonepe.commons.columbus.registry.Registries;
import com.phonepe.commons.columbus.registry.Registry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@TestMethodOrder(OrderAnnotation.class)
class CachedRegistryTest {

    public static final String KEY = "key";
    public static final String VALUE = "value";

    private static final Registry<String, String> delegate = mock();
    private static final Registry<String, String> registry = Registries.cached(delegate);

    @Test
    @Order(1)
    @DisplayName("cache miss calls delegate")
    void testCacheMiss() {
        assertEquals(VALUE, registry.get(KEY).orElse(null));
        verify(delegate, Mockito.times(1)).get(KEY);
        verifyNoMoreInteractions(delegate);
    }

    @Test
    @Order(2)
    @DisplayName("cache hit does not call delegate")
    void testCacheHit() {
        assertEquals(VALUE, registry.get(KEY).orElse(null));
        verifyNoInteractions(delegate);
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
