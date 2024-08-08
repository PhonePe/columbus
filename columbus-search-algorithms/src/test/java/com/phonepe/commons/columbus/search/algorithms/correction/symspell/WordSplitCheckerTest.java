package com.phonepe.commons.columbus.search.algorithms.correction.symspell;

import com.phonepe.commons.columbus.search.algorithms.common.distance.Distance;
import com.phonepe.commons.columbus.search.algorithms.common.distance.keyboard.QwertyDistance;
import com.phonepe.commons.columbus.search.algorithms.common.distance.string.WeightedDamerauLevenshteinDistance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class WordSplitCheckerTest {

    private UnigramCorrector unigramCorrector;

    private WordSplitChecker wordSplitChecker;

    @BeforeEach
    void setup() {
        SymSpellConfig symSpellConfig = new SymSpellConfig();
        Distance<Character> charDistance = new QwertyDistance();
        InMemoryDataHolder inMemoryDataHolder = new InMemoryDataHolder(symSpellConfig);
        Distance<String> stringDistance = WeightedDamerauLevenshteinDistance.builder()
                .unitWeights()
                .charDistance(charDistance)
                .build();
        unigramCorrector = new UnigramCorrector(inMemoryDataHolder, symSpellConfig, stringDistance);
        wordSplitChecker = new WordSplitChecker(unigramCorrector, inMemoryDataHolder, stringDistance, symSpellConfig);
        inMemoryDataHolder.addItem("apple pie", 54);
        inMemoryDataHolder.addItem("apple", 37);
        inMemoryDataHolder.addItem("pie", 38);
        inMemoryDataHolder.addItem("almond", 50);
        inMemoryDataHolder.addItem("oil", 50);
    }

    @Test
    void testCheckSplitWordExists() {
        checkSplitWord("applepi", "applepi", "apple pie");
    }

    @Test
    void testCheckSplitWordExistsNonBigram() {
        checkSplitWord("almondoil", "almondoil", "almond oil");
    }

    @Test
    void testCheckSplitWordDoesNotExists() {
        checkSplitWord("apple", "apple", "apple");
    }

    private void checkSplitWord(final String phrase, final String word, final String expected) {
        List<Suggestion> suggestions = unigramCorrector.correctWord(phrase, 2);
        List<Suggestion> suggestionParts = new ArrayList<>();
        wordSplitChecker.checkSplitWord(suggestionParts, suggestions, word, 2);
        assertEquals(expected, suggestionParts.get(0).getTerm());
    }
}
