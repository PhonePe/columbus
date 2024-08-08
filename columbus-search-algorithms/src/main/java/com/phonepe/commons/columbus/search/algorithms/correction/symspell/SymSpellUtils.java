package com.phonepe.commons.columbus.search.algorithms.correction.symspell;

import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@UtilityClass
public class SymSpellUtils {

    public static Set<String> getEditDeletes(
            String key,
            double maxEditDistance, int prefixLength, double editFactor
    ) {
        Set<String> deletedWords = new HashSet<>();
        if (key.length() <= maxEditDistance) {
            deletedWords.add("");
        }
        if (key.length() > maxEditDistance) {
            deletedWords
                    .add(key.substring(
                            0,
                            Math.min(prefixLength, key.length())));
        }
        return edits(key, 0, deletedWords, getEdistance(maxEditDistance, key.length(), editFactor));
    }

    /**
     * Inexpensive and language independent: only deletes, no transposes + replaces + inserts replaces
     * and inserts are expensive and language dependent
     */
    public static Set<String> edits(
            String word, double editDistance, Set<String> deletedWords,
            final Double maxEd
    ) {
        editDistance++;
        if (word.length() < 1) {
            return deletedWords;
        }

        for (int i = 0; i < word.length(); i++) {
            String delete = word.substring(0, i) + word.substring(i + 1);
            if (deletedWords.add(delete) && editDistance < maxEd) {
                edits(delete, editDistance, deletedWords, maxEd);
            }
        }
        deletedWords.add(word);
        return deletedWords;
    }

    /**
     * Early exit method
     */
    public static List<Suggestion> earlyExit(
            List<Suggestion> suggestionItems,
            String phrase, double maxEditDistance, boolean ignoreUnknown
    ) {
        if (suggestionItems.isEmpty() && !ignoreUnknown) {
            suggestionItems.add(new Suggestion(phrase, maxEditDistance + 1, 0));
        }
        return suggestionItems;
    }

    public static boolean isLessOrEqualDouble(double d1, double d2, final double threshold) {
        return Math.abs(d1 - d2) < threshold || d1 < d2;
    }

    public static boolean isLessDouble(double d1, double d2, final double threshold) {
        return !isEqualDouble(d1, d2, threshold) && d1 < d2;
    }

    public static boolean isEqualDouble(double d1, double d2, final double threshold) {
        return Math.abs(d1 - d2) < threshold;
    }

    /**
     * Check if heads  are same
     */
    public static boolean continueConditionIfHeadIsSame(
            List<Suggestion> suggestions,
            List<Suggestion> suggestions1
    ) {
        return suggestions1.isEmpty() || !suggestions.isEmpty() && suggestions.get(0).equals(suggestions1.get(0));
    }

    public boolean checkHeadIsSameCondition(
            List<Suggestion> suggestions,
            List<Suggestion> suggestions1,
            List<Suggestion> suggestions2
    ) {
        return SymSpellUtils.continueConditionIfHeadIsSame(suggestions, suggestions1)
                || SymSpellUtils.continueConditionIfHeadIsSame(suggestions, suggestions2);
    }

    public static Suggestion joinSuggestionParts(List<Suggestion> suggestionParts) {
        String joinedTerm = "";
        double joinedCount = Double.MAX_VALUE;
        for (Suggestion suggestion : suggestionParts) {
            joinedTerm = joinedTerm.concat(suggestion.getTerm()).concat(" ");
            joinedCount = Math.min(joinedCount, suggestion.getCount());
        }
        joinedTerm = joinedTerm.trim();
        return new Suggestion(joinedTerm, Double.MAX_VALUE, joinedCount);
    }

    private static Double getEdistance(double maxEditDistance, int length, double factor) {
        double computedEd = Math.round(factor * length);
        if (Math.min(maxEditDistance, computedEd) == maxEditDistance) {
            return maxEditDistance;
        }
        return computedEd;
    }
}
