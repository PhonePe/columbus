package com.phonepe.commons.columbus.common.initializer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LazyInitTest {

    @Test
    @DisplayName("lazy initialize value")
    void testLazyPresent() {
        final var value = new LazyInit<>(() -> 10);

        assertTrue(value.isPresent(), "value can be initialized");
        assertEquals(10, value.get(), "value must match the supplied value");
        assertTrue(value.isPresent(), "value is already initialized");
    }

    @Test
    @DisplayName("initialize only once")
    void testInitializeExactlyOnce() {
        final var count = new AtomicInteger(0);
        final var value = new LazyInit<>(() -> {
            count.incrementAndGet();
            return 10;
        });

        assertTrue(value.isPresent(), "value can be initialized");
        assertEquals(0, count.get(), "supplier is not invoked until get is called");
        assertEquals(10, value.get(), "value must match the supplied value");
        assertTrue(value.isPresent(), "value is already initialized");
        assertEquals(1, count.get(), "supplier is invoked to get the value");
        assertEquals(10, value.get(), "value must not change");
        assertEquals(1, count.get(), "supplier is invoked only once");
    }

    @Test
    @DisplayName("cannot initialize due to precondition")
    void testCannotInitialize() {
        final var value = new LazyInit<>(() -> false, () -> 10);

        assertFalse(value.isPresent(), "value cannot be initialized");
        assertThrows(IllegalStateException.class, value::get, "initialization fails if precondition is not satisfied");
        assertFalse(value.isPresent(), "value is not initialized");
    }

    @Test
    @DisplayName("dependency using precondition")
    void testDependency() {
        final var lateValue = new LateInit<Integer>();
        final var value = new LazyInit<>(lateValue::isPresent, () -> lateValue.get() + 10);

        assertFalse(lateValue.isPresent(), "late value is not initialized");
        assertFalse(value.isPresent(), "lazy value cannot be initialized");
        lateValue.set(5);
        assertTrue(lateValue.isPresent(), "late value is initialized");
        assertEquals(5, lateValue.get(), "late value must match the provided value");
        assertTrue(value.isPresent(), "lazy value can be initialized");
        assertEquals(15, value.get(), "lazy value must match the created value");
    }
}
