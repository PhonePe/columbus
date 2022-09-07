package com.phonepe.platform.columbus.search.algorithms.correction.symspell;

import com.phonepe.platform.columbus.search.algorithms.common.distance.Distance;
import com.phonepe.platform.columbus.search.algorithms.correction.Suggestion;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class WordSplitChecker {
    private final UnigramCorrector unigramCorrector;
    private final InMemoryDataHolder inMemoryDataHolder;
    private final Distance<String> stringDistance;

    private static final Long N_MAX = 1024908267229L;

    private final SymSpellConfig symSpellConfig;

    /**
     * Checks if the given word can be corrected by splitting it into two words
     * and adding a space
     * example: for the word applepi it adds a space in between apple pie
     * incase the split is possible, it updates the suggestion parts, to be used
     * in the result
     */
    @SuppressWarnings("java:S135")
    void checkSplitWord(List<Suggestion> suggestionParts, List<Suggestion> suggestions, String word, double maxEditDistance) {
        Suggestion suggestionSplitBest = null;
        if (!suggestions.isEmpty()) {
            suggestionSplitBest = suggestions.get(0);
        }

        if (word.length() <= 1) {
            suggestionParts.add(new Suggestion(word, maxEditDistance + 1, 0));
            return;
        }
        for (int j = 1; j < word.length(); j++) {
            String part1 = word.substring(0, j);
            String part2 = word.substring(j);
            List<Suggestion> splitSuggestionLeft = unigramCorrector.correctWord(part1, maxEditDistance);
            List<Suggestion> splitSuggestionRight = unigramCorrector.correctWord(part2, maxEditDistance);
            if (SymSpellUtils.checkHeadIsSameCondition(suggestions, splitSuggestionLeft, splitSuggestionRight)) {
                continue;
            }
            String split = splitSuggestionLeft.get(0).getTerm() + " " + splitSuggestionRight.get(0).getTerm();
            double splitDistance = stringDistance.distance(word, split, maxEditDistance);
            double count;


            splitDistance = updateSplitDistance(splitDistance, maxEditDistance);


            if (!checkSuggestionAsSplitExists(splitDistance, suggestionSplitBest)) {
                continue;
            }
            Double bigramFreq = inMemoryDataHolder.getItemFrequencyBiGram(split);
            if (bigramFreq != null) {
                count = calculateBigramCount(suggestions, splitSuggestionLeft, splitSuggestionRight, word);
            } else {
                count = Math.min(symSpellConfig.getBigramCountMin(),
                    (splitSuggestionLeft.get(0).getCount() / N_MAX
                        * splitSuggestionRight.get(0).getCount()));
            }
            Suggestion suggestionSplit = new Suggestion(split, splitDistance, count);
            if ((suggestionSplitBest == null) || suggestionSplit.getDistance() > splitDistance
                || suggestionSplitBest.getCount() < count) {
                suggestionSplitBest = suggestionSplit;
            }
        }
        if (suggestionSplitBest != null) {
            suggestionParts.add(suggestionSplitBest);
        } else if (!symSpellConfig.isIgnoreUnknown()) {
            suggestionParts.add(new Suggestion(word, maxEditDistance + 1, 0));
        }

    }

    private double updateSplitDistance(double splitDistance, double maxEditDistance) {
        if (splitDistance < 0) {
            splitDistance = maxEditDistance + 1;
        }
        return splitDistance;
    }

    private boolean checkSuggestionAsSplitExists(double splitDistance, Suggestion suggestionSplitBest) {
        if (suggestionSplitBest == null) {
            return true;
        } else return (splitDistance <= suggestionSplitBest.getDistance());
    }

    /**
     * Calculates compound frequency if the split also exists as a bigram
     *For split which exists as a bigram and also have one/both parts in the dictionary as unigrams
     * the frequency is increased accordingly to account of it
     */
    private double calculateBigramCount(List<Suggestion> suggestions, List<Suggestion> splitSuggestionLeft, List<Suggestion> splitSuggestionRight, String word) {
        final Suggestion leftSuggestion = splitSuggestionLeft.get(0);
        final Suggestion rightSuggestion = splitSuggestionRight.get(0);
        final String leftTerm = leftSuggestion.getTerm();
        final String rightTerm = rightSuggestion.getTerm();
        double count = inMemoryDataHolder.getItemFrequencyBiGram(leftTerm + " " + rightTerm);
        if (!suggestions.isEmpty()) {
            final Suggestion suggestion = suggestions.get(0);
            final String term = suggestion.getTerm();
            if (word.equals(leftTerm + rightTerm)) {
                count = suggestion.getCount() + 2;
            } else if (leftTerm.equals(term) || rightTerm.equals(term)) {
                count = Math.max(count, suggestion.getCount() + 1);
            }
        } else if (word.equals(leftTerm + rightTerm)) {
            count = Math.max(count,
                Math.max(leftSuggestion.getCount(), rightSuggestion.getCount()));
        }
        return count;
    }
}
