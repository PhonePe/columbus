package com.phonepe.columbus.common.initializer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LateInitTest {

    @Test
    @DisplayName("no initial value")
    void testCreateEmpty() {
        final var holder = new LateInit<Integer>();

        assertFalse(holder.isPresent(), "holder must not be initialized");
    }

    @Test
    @DisplayName("initialize with value")
    void testCreateWithValue() {
        final var holder = new LateInit<>(10);

        assertAll(
                "holder",
                () -> assertTrue(holder.isPresent(), "holder must be initialized"),
                () -> assertEquals(10, holder.get(), "holder must have the initial value")
        );
    }

    @Test
    @DisplayName("initialize value after creation")
    void testInitialize() {
        final var holder = new LateInit<Integer>();

        assertTrue(holder.initialize(10), "initialize must return true if the value is set successfully");
        assertEquals(10, holder.get(), "holder must be initialized with the given value");
        assertFalse(holder.initialize(20), "initialize must return false if the holder already has a value");
        assertEquals(10, holder.get(), "holder must retain the initial value");
    }

    @Test
    @DisplayName("set value")
    void testSet() {
        final var holder = new LateInit<Integer>();

        assertFalse(holder.isPresent(), "holder must not be initialized");

        holder.set(20);
        assertTrue(holder.isPresent(), "holder must be initialized");
        assertEquals(20, holder.get(), "holder must be initialized with the given value");

        assertThrows(IllegalStateException.class, () -> holder.set(10), "holder cannot be set more than once");
    }

    @Test
    @DisplayName("get value")
    void testGet() {
        final var holder = new LateInit<Integer>();

        assertFalse(holder.isPresent(), "holder must not be initialized");

        assertThrows(IllegalStateException.class, holder::get, "holder has no value to get");

        holder.set(20);
        assertTrue(holder.isPresent(), "holder must be initialized");
        assertEquals(20, holder.get(), "holder must be initialized with the given value");
    }

    @Test
    @DisplayName("create and set value if empty")
    void testInitializeWithSupplier() {
        final var holder = new LateInit<Integer>();

        assertFalse(holder.isPresent(), "holder must not be initialized");

        assertTrue(holder.initializeWith(() -> 20));
        assertTrue(holder.isPresent(), "holder must be initialized");
        assertEquals(20, holder.get(), "holder must be initialized with the given value");

        assertFalse(holder.initializeWith(() -> 10));
        assertEquals(20, holder.get(), "holder must not be overwritten");
    }
}
