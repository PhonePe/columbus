package com.phonepe.columbus.search.algorithms.correction.symspell;

import com.phonepe.columbus.search.algorithms.common.distance.Distance;
import com.phonepe.columbus.search.algorithms.common.distance.keyboard.QwertyDistance;
import com.phonepe.columbus.search.algorithms.common.distance.string.WeightedDamerauLevenshteinDistance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

@ExtendWith({MockitoExtension.class})
class CombineWordsCheckerTest {

    private CombineWordsChecker combineWordsChecker;

    @Mock
    private UnigramCorrector unigramCorrector;

    @BeforeEach
    void setUp() {
        Distance<Character> charDistance = new QwertyDistance();
        Distance<String> stringDistance = WeightedDamerauLevenshteinDistance.builder()
                .unitWeights()
                .charDistance(charDistance)
                .build();
        combineWordsChecker = new CombineWordsChecker(unigramCorrector, stringDistance);
    }

    @Test
    void checkCombinationExistsTrue() {
        ArrayList<Suggestion> currWordSuggestion = new ArrayList<>();
        currWordSuggestion.add(new Suggestion("pieces", 2, 3));
        doReturn(List.of(new Suggestion("processed", 0, 38))).when(unigramCorrector).correctWord("processed", 2);
        boolean canCombine = combineWordsChecker.canBeCombined(
                CombineLookupContext.builder()
                        .maxEditDistance(2)
                        .previousToken("proces")
                        .token("sed")
                        .build(),
                currWordSuggestion,
                List.of(new Suggestion("red", 1, 34)));
        assertTrue(canCombine);
    }

    @Test
    void checkCombinationExistsFalse() {
        ArrayList<Suggestion> currWordSuggestion = new ArrayList<>();
        currWordSuggestion.add(new Suggestion("apple", 0, 34));
        doReturn(List.of(new Suggestion("processed", 0, 38))).when(unigramCorrector).correctWord("applejuice", 2);
        boolean canCombine = combineWordsChecker.canBeCombined(
                CombineLookupContext.builder()
                        .maxEditDistance(2)
                        .previousToken("apple")
                        .token("juice")
                        .build(),
                currWordSuggestion,
                List.of(new Suggestion("juice", 0, 34)));
        assertFalse(canCombine);
    }
}
