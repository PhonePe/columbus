package com.phonepe.columbus.common;

import com.phonepe.columbus.common.initializer.LateInit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class PredicatesTest {

    @Nested
    class NonNullConsumerTest {

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
    class ConditionalConsumerTest {

        private static final Predicate<Integer> CONDITION = integer -> integer > 5;

        @Test
        @DisplayName("value is consumed when condition is satisfied")
        void testApplyWhenConditionSatisfied() {
            final var holder = new LateInit<Integer>();

            Predicates.applyIf(10, CONDITION, holder::set);

            assertEquals(10, holder.get(), "consumer must be called");
        }

        @Test
        @DisplayName("value is ignored when condition is not satisfied")
        void testDoesNotApplyWhenConditionNotSatisfied() {
            final var holder = new LateInit<Integer>();

            Predicates.applyIf(1, CONDITION, holder::set);

            assertFalse(holder.isPresent(), "consumer must not be called");
        }
    }

    @Nested
    class PositiveIntegerConsumerTest {

        @Test
        @DisplayName("value is consumed when positive")
        void testApplyWhenPositive() {
            final var holder = new LateInit<Integer>();

            Predicates.applyIfPositive(10, holder::set);

            assertEquals(10, holder.get(), "consumer must be called");
        }

        @Test
        @DisplayName("value is ignored when negative")
        void testDoesNotApplyWhenNegative() {
            final var holder = new LateInit<Integer>();

            Predicates.applyIfPositive(-10, holder::set);

            assertFalse(holder.isPresent(), "consumer must not be called");
        }

        @Test
        @DisplayName("value is ignored when zero")
        void testDoesNotApplyWhenZero() {
            final var holder = new LateInit<Integer>();

            Predicates.applyIfPositive(0, holder::set);

            assertFalse(holder.isPresent(), "consumer must not be called");
        }

        @Test
        @DisplayName("value is ignored when null")
        void testDoesNotApplyWhenNull() {
            final var holder = new LateInit<Integer>();

            Predicates.applyIfPositive(null, holder::set);

            assertFalse(holder.isPresent(), "consumer must not be called");
        }
    }

    @Nested
    class NonNegativeIntegerConsumerTest {

        @Test
        @DisplayName("value is consumed when positive")
        void testApplyWhenPositive() {
            final var holder = new LateInit<Integer>();

            Predicates.applyIfNonNegative(10, holder::set);

            assertEquals(10, holder.get(), "consumer must be called");
        }

        @Test
        @DisplayName("value is ignored when negative")
        void testDoesNotApplyWhenNegative() {
            final var holder = new LateInit<Integer>();

            Predicates.applyIfNonNegative(-10, holder::set);

            assertFalse(holder.isPresent(), "consumer must not be called");
        }

        @Test
        @DisplayName("value is consumed when zero")
        void testDoesNotApplyWhenZero() {
            final var holder = new LateInit<Integer>();

            Predicates.applyIfNonNegative(0, holder::set);

            assertEquals(0, holder.get(), "consumer must be called");
        }

        @Test
        @DisplayName("value is ignored when null")
        void testDoesNotApplyWhenNull() {
            final var holder = new LateInit<Integer>();

            Predicates.applyIfPositive(null, holder::set);

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
