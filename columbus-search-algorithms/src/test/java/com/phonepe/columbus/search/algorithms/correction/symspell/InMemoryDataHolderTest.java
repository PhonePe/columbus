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
package com.phonepe.columbus.search.algorithms.correction.symspell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryDataHolderTest {

    private static final List<String> APPLE_DELETES = List.of(
            "pple", "aple", "appl", "appe", "ple", "ppe", "ppl", "ape", "ale", "apl", "app", "ppl");

    private InMemoryDataHolder inMemoryDataHolder;

    @BeforeEach
    void setUp() {
        SymSpellConfig symSpellConfig = new SymSpellConfig();
        inMemoryDataHolder = new InMemoryDataHolder(symSpellConfig);
    }

    @Test
    void testAddItem() {
        boolean first = inMemoryDataHolder.addItem("apple", 10.0);
        assertTrue(first);
        assertEquals(APPLE_DELETES.size(), inMemoryDataHolder.getDeletes().size());
        for (String delete : APPLE_DELETES) {
            assertEquals(List.of("apple"), inMemoryDataHolder.getDeletes(delete));
        }
        boolean second = inMemoryDataHolder.addItem("apple", 10.0);
        assertFalse(second);
    }

    @Test
    void testAddItemNegativeFreq() {
        boolean added = inMemoryDataHolder.addItem("apple", -1);
        assertFalse(added);
    }

    @Test
    void testGetItemFrequency() {
        inMemoryDataHolder.addItem("apple", 10.0);
        Double frequency = inMemoryDataHolder.getItemFrequency("apple");
        assertEquals(10.0, frequency);
        frequency = inMemoryDataHolder.getItemFrequency("abcd");
        assertNull(frequency);
    }

    @Test
    void testGetItemFrequencyBiGram() {
        inMemoryDataHolder.addItem("abc def", 10.0);
        Double frequency = inMemoryDataHolder.getItemFrequencyBiGram("abc def");
        assertEquals(10.0, frequency);
        frequency = inMemoryDataHolder.getItemFrequencyBiGram("def ghi");
        assertNull(frequency);
    }

    @Test
    void testGetDeletes() {
        inMemoryDataHolder.addItem("apple", 10.0);
        for (String delete : APPLE_DELETES) {
            assertEquals("apple", inMemoryDataHolder.getDeletes(delete).get(0));
        }
    }
}
