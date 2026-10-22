/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phonepe.columbus.common;

import com.phonepe.columbus.search.sort.SortOrder;
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
