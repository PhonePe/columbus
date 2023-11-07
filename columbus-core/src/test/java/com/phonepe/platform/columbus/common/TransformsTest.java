package com.phonepe.platform.columbus.common;

import com.phonepe.platform.columbus.search.sort.SortOrder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransformsTest {

    @Nested
    class EnumValueTest {

        @Test
        @DisplayName("valid enum value")
        void testValidEnumValue() {
            final var value = Transforms.toEnumValue("ASC", "sort order", SortOrder::valueOf);

            assertEquals(SortOrder.ASC, value, "must return valid enum");
        }

        @Test
        @DisplayName("invalid enum value")
        void testInvalidEnumValue() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Transforms.<SortOrder>toEnumValue("asc", "sort order", SortOrder::valueOf),
                    "must throw on invalid enum");
        }
    }

    @Nested
    class IntegerParseTest {

        @Test
        @DisplayName("already Integer")
        void testInteger() {
            final var integer = Transforms.parseInteger(10);

            assertEquals(10, integer, "must return provided integer");
        }

        @Test
        @DisplayName("valid String")
        void testValidString() {
            final var integer = Transforms.parseInteger("10");

            assertEquals(10, integer, "must return parsed string");
        }

        @Test
        @DisplayName("invalid String")
        void testInvalidString() {
            final var integer = Transforms.parseInteger("A10");

            assertNull(integer, "must return default if invalid");
        }

        @Test
        @DisplayName("invalid")
        void testInvalidObject() {
            final var integer = Transforms.parseInteger(new Object(), 20);

            assertEquals(20, integer, "must return default value");
        }
    }
}
