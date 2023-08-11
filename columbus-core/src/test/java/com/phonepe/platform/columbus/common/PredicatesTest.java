package com.phonepe.platform.columbus.common;

import com.phonepe.platform.columbus.common.initializer.LateInit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class PredicatesTest {

    @Nested
    class ConsumerTest {

        @Test
        @DisplayName("non null value is consumed")
        void testApplyWhenNotNull() {
            final var holder = new LateInit<Integer>();

            Predicates.applyIfNotNull(10, holder::set);

            assertEquals(10, holder.get(), "consumer must be called");
        }

        @Test
        @DisplayName("null value is ignored")
        void testDoesNotApplyWhenNull() {
            final var holder = new LateInit<Integer>();

            Predicates.applyIfNotNull(null, holder::set);

            assertFalse(holder.isPresent(), "consumer must not be called");
        }
    }

    @Nested
    class TransformedConsumerTest {

        @Test
        @DisplayName("non null value is consumed")
        void testApplyWhenNotNull() {
            final var holder = new LateInit<Integer>();

            Predicates.applyIfNotNull("abc", String::length, holder::set);

            assertEquals(3, holder.get(), "consumer must be called");
        }

        @Test
        @DisplayName("null value is ignored")
        void testDoesNotApplyWhenNull() {
            final var holder = new LateInit<Integer>();

            Predicates.applyIfNotNull(null, String::length, holder::set);

            assertFalse(holder.isPresent(), "consumer must not be called");
        }

        @Test
        @DisplayName("null transformed value is ignored")
        void testDoesNotApplyWhenTransformedToNull() {
            final var holder = new LateInit<Integer>();

            Predicates.applyIfNotNull("abc", string -> string.length() > 5 ? string.length() : null, holder::set);

            assertFalse(holder.isPresent(), "consumer must not be called");
        }
    }
}
