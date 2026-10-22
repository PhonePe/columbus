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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ElementsTest {

    @Test
    @DisplayName("get first non null")
    void testFirstNonNull() {
        final var list = List.of(10, 20);

        final var element = Elements.firstOrNull(list);

        assertEquals(10, element, "first element must be returned");
    }

    @Test
    @DisplayName("get null if empty")
    void testNullIfEmpty() {
        final var list = List.of();

        final var element = Elements.firstOrNull(list);

        assertNull(element, "null must be returned for empty lists");
    }

    @Test
    @DisplayName("get null if null")
    void testNullIfNull() {
        assertNull(Elements.firstOrNull(null), "null must be returned for null inputs");
    }

    @Test
    @DisplayName("get default if index out of bounds")
    void testGetDefaultIfOutOfBounds() {
        final var list = List.of(10, 20);

        assertEquals(0, Elements.getOrDefault(list, 3, 0), "default value must be returned");
        assertEquals(0, Elements.getOrDefault(list, -3, 0), "default value must be returned");
    }

    @Test
    @DisplayName("get or default from map")
    void testGetOrDefaultMap() {
        final var map = Map.<String, Object>of(
                "a", 1,
                "b", 2L
        );

        assertEquals(1, Elements.getOrNull(map, "a", Integer.class), "value from map must be returned");

        assertEquals(2L, Elements.getOrNull(map, "b", Long.class), "value from map must be returned");
        assertNull(Elements.getOrNull(map, "b", Integer.class), "default value must be returned");
    }

    @Test
    @DisplayName("get or default from empty map")
    void testGetOrDefaultEmptyMap() {
        assertEquals(3, Elements.getOrDefault(null, "a", 3, Integer.class), "default value must be returned");
        assertNull(Elements.getOrNull(null, "a", Integer.class), "default value must be returned");
    }
}
