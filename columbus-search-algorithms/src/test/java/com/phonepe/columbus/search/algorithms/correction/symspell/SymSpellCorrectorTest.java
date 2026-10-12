package com.phonepe.columbus.search.algorithms.correction.symspell;

import com.phonepe.columbus.search.algorithms.common.distance.Distance;
import com.phonepe.columbus.search.algorithms.common.distance.keyboard.QwertzDistance;
import com.phonepe.columbus.search.algorithms.common.distance.string.WeightedDamerauLevenshteinDistance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SymSpellCorrectorTest {

    private SymSpellCorrector symSpellCorrector;

    @BeforeEach
    void beforeEach() {
        SymSpellConfig symSpellConfig = new SymSpellConfig();
        Distance<Character> charDistance = new QwertzDistance();
        Distance<String> stringDistance = WeightedDamerauLevenshteinDistance.builder()
                .unitWeights()
                .charDistance(charDistance)
                .build();
        InMemoryDataHolder inMemoryDataHolder = new InMemoryDataHolder(symSpellConfig);
        UnigramCorrector unigramCorrector = new UnigramCorrector(inMemoryDataHolder, symSpellConfig, stringDistance);
        CombineWordsChecker combineWordsChecker = new CombineWordsChecker(unigramCorrector, stringDistance);
        WordSplitChecker wordSplitChecker = new WordSplitChecker(
                unigramCorrector, inMemoryDataHolder, stringDistance, symSpellConfig);
        symSpellCorrector = new SymSpellCorrector(
                unigramCorrector, combineWordsChecker, wordSplitChecker, symSpellConfig, stringDistance);
        inMemoryDataHolder.addItem("britania", 38);
        inMemoryDataHolder.addItem("good", 54);
        inMemoryDataHolder.addItem("day", 48);
        inMemoryDataHolder.addItem("good day", 54);
        inMemoryDataHolder.addItem("cookies", 43);
    }

    @Test
    void testCorrectQuery() {
        List<Suggestion> res = symSpellCorrector.correctQuery("britanea goodday kookies", 2);
        assertEquals("britania good day cookies", res.get(0).getTerm());
    }

    @Test
    void testCorrectQueryWithCombine() {
        List<Suggestion> res = symSpellCorrector.correctQuery("brit ania goodday kookies", 2);
        assertEquals("britania good day cookies", res.get(0).getTerm());
    }

    @Test
    void testMaxEditDistanceGreaterThanConfigured() {
        assertThrows(IllegalArgumentException.class, () -> symSpellCorrector.correctQuery("britanea", 5));
    }

    @Test
    void testEmptyString() {
        assertThrows(IllegalArgumentException.class, () -> symSpellCorrector.correctQuery("", 5));
    }
}
