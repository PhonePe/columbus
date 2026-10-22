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

import com.phonepe.columbus.search.algorithms.common.distance.Distance;
import com.phonepe.columbus.search.algorithms.common.distance.string.WeightedDamerauLevenshteinDistance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SymSpellClientTest {

    private static final SymSpellConfig SYMSPELL_CONFIG = new SymSpellConfig();

    private static final Distance<String> STRING_DISTANCE = WeightedDamerauLevenshteinDistance.builder()
            .unitWeights()
            .build();
    private InMemoryDataHolder inMemoryDataHolder;
    private SymSpellClient symSpellClient;

    @BeforeEach
    void setup() {
        inMemoryDataHolder = new InMemoryDataHolder(SYMSPELL_CONFIG);
        UnigramCorrector unigramCorrector = new UnigramCorrector(inMemoryDataHolder, SYMSPELL_CONFIG, STRING_DISTANCE);
        CombineWordsChecker combineWordsChecker = new CombineWordsChecker(unigramCorrector, STRING_DISTANCE);
        WordSplitChecker wordSplitChecker = new WordSplitChecker(
                unigramCorrector, inMemoryDataHolder, STRING_DISTANCE, SYMSPELL_CONFIG);
        SymSpellCorrector symSpellCorrector = new SymSpellCorrector(
                unigramCorrector, combineWordsChecker, wordSplitChecker, SYMSPELL_CONFIG, STRING_DISTANCE);
        symSpellClient = new SymSpellClient(symSpellCorrector, inMemoryDataHolder);
        inMemoryDataHolder.addItem("processed", 50);
        inMemoryDataHolder.addItem("cheese", 47);
        inMemoryDataHolder.addItem("processed cheese", 51);
    }

    @Test
    void testFind() {
        List<String> res = symSpellClient.process("processedcheese zzzzzz", 5);
        assertEquals("processed cheese", res.get(0));
    }

    @Test
    void testFindDefault() {
        String res = symSpellClient.process("processedcheese zzzzzz");
        assertEquals("processed cheese", res);
    }

    @Test
    void testClear() {
        symSpellClient.clear();
        assertEquals(0, inMemoryDataHolder.getBigramsDictionary().size());
        assertEquals(0, inMemoryDataHolder.getWordsDictionary().size());
        assertEquals(0, inMemoryDataHolder.getDeletes().size());
    }
}
