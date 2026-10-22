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
package com.phonepe.columbus.common.iterator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FlatteningIteratorTest {

    private static final List<List<Integer>> LIST = List.of(List.of(1, 2, 3), List.of(4, 5), List.of(6));

    @Test
    @DisplayName("no value")
    void testEmpty() {
        final var iterator = Iterators.flattening(Collections.emptyIterator());

        assertFalse(iterator.hasNext(), "no elements to partition");
        assertThrows(NoSuchElementException.class, iterator::next, "next() throws if no element left");
    }

    @Test
    @DisplayName("flatten all lists")
    void testFlatten() {
        final var iterator = Iterators.flattening(LIST.iterator());

        for (int i = 1; i <= 6; i++) {
            assertTrue(iterator.hasNext(), "elements present in partition");
            assertEquals(i, iterator.next(), "elements must be in order");
        }
        assertFalse(iterator.hasNext(), "no elements left in partition");
        assertThrows(NoSuchElementException.class, iterator::next, "next() throws if no element left");
    }
}
