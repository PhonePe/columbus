package com.phonepe.platform.columbus.internal.registry;

import com.phonepe.platform.columbus.registry.MutableRegistry;
import com.phonepe.platform.columbus.registry.Registries;
import com.phonepe.platform.columbus.registry.Registry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class MonitoredRegistryTest {

    private static final MonitoredRegistry.Listener<String, Integer> listener = mock();
    private static final MutableRegistry<String, Integer> delegate = Registries.stored();

    private static final Registry<String, Integer> registry = Registries.monitorIfNotEmpty(delegate, listener);

    @Test
    @DisplayName("listener success")
    void testListenerSuccess() {
        assertEquals(10, registry.get("key1").orElse(null));
        verify(listener, times(1)).success("key1", 10);
        verifyNoMoreInteractions(listener);
    }

    @Test
    @DisplayName("listener failure")
    void testListenerFailure() {
        assertTrue(registry.get("missing").isEmpty());
        verify(listener, times(1)).failure("missing");
        verifyNoMoreInteractions(listener);
    }

    @AfterEach
    @SuppressWarnings("unchecked")
    void tearDown() {
        reset(listener);
    }

    @BeforeAll
    static void beforeAll() {
        delegate.register("key1", 10);
    }
}
