package com.phonepe.platform.columbus.search.algorithms.correction.symspell;

import com.phonepe.platform.columbus.core.exception.ColumbusException;
import com.phonepe.platform.columbus.models.search.exceptions.ColumbusSearchErrorCode;
import com.phonepe.platform.columbus.search.algorithms.common.distance.Distance;
import com.phonepe.platform.columbus.search.algorithms.common.distance.keyboard.QwertyDistance;
import com.phonepe.platform.columbus.search.algorithms.common.distance.string.WeightedDamerauLevenshteinDistance;
import com.phonepe.platform.columbus.search.algorithms.correction.Suggestion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UnigramCorrectorTest {
    private UnigramCorrector unigramCorrector;

    @BeforeEach
    void setUp() {
        SymSpellConfig symSpellConfig = new SymSpellConfig();
        InMemoryDataHolder inMemoryDataHolder = new InMemoryDataHolder(symSpellConfig);
        Distance<Character> charDistance = new QwertyDistance();
        Distance<String> stringDistance = WeightedDamerauLevenshteinDistance.builder()
            .unitWeights()
            .charDistance(charDistance)
            .build();
        unigramCorrector = new UnigramCorrector(inMemoryDataHolder, symSpellConfig, stringDistance);
        inMemoryDataHolder.addItem("apple", 38);
        inMemoryDataHolder.addItem("a", 57);
    }

    @Test
    void testCorrectWord() {
        List<Suggestion> correctedWord = unigramCorrector.correctWord("appil", 2);
        assertEquals("apple", correctedWord.get(0).getTerm());
    }

    @Test
    void testCorrectWordInvalidMaxEditDistance() {
        var exception = assertThrows(ColumbusException.class, () -> unigramCorrector.correctWord("appil", 3));
        assertEquals(ColumbusSearchErrorCode.LOOKUP_ERROR, exception.getErrorCode());
    }

    @Test
    void testCorrectWordNegativeEditDistance() {
        List<Suggestion> correctedWord = unigramCorrector.correctWord("appil", -2);
        assertEquals("apple", correctedWord.get(0).getTerm());
    }

    @Test
    void testSingularWordCheck() {
        List<Suggestion> correctedWord = unigramCorrector.correctWord("a", 2);
        assertEquals("a", correctedWord.get(0).getTerm());
    }

    @Test
    void testCorrectWordNoCorrectionExists() {
        List<Suggestion> correctedWord = unigramCorrector.correctWord("banana", 2);
        assertEquals(0, correctedWord.size());
    }
}