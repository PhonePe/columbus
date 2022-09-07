package com.phonepe.platform.columbus.search.algorithms.correction.symspell;

import com.phonepe.platform.columbus.search.algorithms.common.distance.Distance;
import com.phonepe.platform.columbus.search.algorithms.correction.Suggestion;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class CombineWordsChecker {
    private final UnigramCorrector unigramCorrector;
    private final Distance<String> stringDistance;

    /**
     * Checks if the current word can be combined with the previous word
     * for eg, the word "brita nia" can be combined to make a word britania
     * even though brita can be corrected as britain and nia can be corrected
     * as mania, the edit distance for britania is less than that of britain mania
     * hence the word is combined
     */
    public boolean canBeCombined(final CombineLookupContext combineLookupContext, List<Suggestion> suggestionParts, List<Suggestion> currentWordSuggestions) {
        String token = combineLookupContext.getToken();
        String previousToken = combineLookupContext.getPreviousToken();
        double maxEditDistance = combineLookupContext.getMaxEditDistance();
        List<Suggestion> combinedSuggestion = unigramCorrector.correctWord(previousToken + token, maxEditDistance);
        if (combinedSuggestion.isEmpty()) {
            return false;
        }
        if (suggestionParts.isEmpty()) {
            combinedSuggestion.get(0).setDistance(combinedSuggestion.get(0).getDistance() + 1);
            suggestionParts.add(combinedSuggestion.get(0));
            return true;
        }
        Suggestion suggestionForPreviousWord = suggestionParts.get(suggestionParts.size() - 1);
        Suggestion bestFitForCurrentWord = currentWordSuggestions.isEmpty() ? new Suggestion(token, maxEditDistance + 1, 0) : currentWordSuggestions.get(0);
        double editDistance = stringDistance.distance(suggestionForPreviousWord.getTerm().concat(" ").concat(bestFitForCurrentWord.getTerm())
            , previousToken.concat(" ").concat(token));
        if (combinedSuggestion.get(0).getDistance() < editDistance) {
            combinedSuggestion.get(0).setDistance(combinedSuggestion.get(0).getDistance() + 1);
            suggestionParts.remove(suggestionParts.size() - 1);
            suggestionParts.add(combinedSuggestion.get(0));
            return true;
        }
        return false;
    }
}
