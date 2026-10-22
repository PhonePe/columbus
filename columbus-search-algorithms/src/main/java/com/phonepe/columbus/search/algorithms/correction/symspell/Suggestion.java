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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;

@Getter
@Setter
@EqualsAndHashCode
@SuppressWarnings("java:S1210") // Sonar does not identify Lombok's equals and hashcode
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
     * final similarity
     */
    private double score;

    public Suggestion(String term, double distance, double count) {
        this.term = term;
        this.distance = distance;
        this.count = count;
    }

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
