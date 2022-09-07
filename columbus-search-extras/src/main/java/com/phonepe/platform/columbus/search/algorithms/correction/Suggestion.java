package com.phonepe.platform.columbus.search.algorithms.correction;

import com.phonepe.platform.columbus.search.algorithms.correction.symspell.SymSpellUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;

@Getter
@Setter
@EqualsAndHashCode
public class Suggestion implements Comparator<Suggestion>, Comparable<Suggestion> {
    /**
     * The suggested correctly spelled word.
     */
    private String term;

    /**
     * Edit distance between searched for word and suggestion.
     */
    private double distance;

    /**
     * Frequency of suggestion in the dictionary (a measure of how common the word is).
     */
    private double count;

    /**
     * COnstructor  for SuggestionItem
     *
     * @param term
     * @param distance
     * @param count
     */
    public Suggestion(String term, double distance, double count) {
        this.term = term;
        this.distance = distance;
        this.count = count;
    }

    /**
     * final similarity
     */
    private double score;

    /**
     * Comparison to use in Sorting: Prefernce given to distance, and if distance is same then count
     */
    @Override
    public int compareTo(Suggestion other) {
        if (SymSpellUtils.isEqualDouble(this.distance, other.distance, 0.001f)) {
            return Double.compare(other.count, this.count);
        }
        return Double.compare(this.distance, other.distance);
    }

    @Override
    public int compare(Suggestion suggestion, Suggestion suggestion2) {
        return suggestion.compareTo(suggestion2);
    }

}
