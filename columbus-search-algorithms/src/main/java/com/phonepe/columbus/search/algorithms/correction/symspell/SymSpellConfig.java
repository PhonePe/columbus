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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("java:S1170") // Sonar does not identify Lombok's builder default
public class SymSpellConfig {

    /**
     * Default verbosity {@link Verbosity}
     */
    @Builder.Default
    private final Verbosity verbosity = Verbosity.ALL;

    /**
     * limit suggestion list to topK entries
     */
    @Builder.Default
    private final int topK = 5; // limits result to n entries

    /**
     * Damerau function variables Deletion weight: 1.20 ~ 1.40
     */
    @Builder.Default
    private final float deletionWeight = 1.0f;

    /**
     * Damerau function variables Insertion weight: 1.01
     */
    @Builder.Default
    private final float insertionWeight = 1.0f;

    /**
     * Damerau function variables Replace weight: 0.9f ~ 1.20
     */
    @Builder.Default
    private final float replaceWeight = 1.0f;
    /**
     * Damerau function variables Transposition weight: 0.7f ~ 1.05
     */
    @Builder.Default
    private final float transpositionWeight = 1.0f;

    /**
     * true if the spellchecker should lowercase terms
     */
    @Builder.Default
    private final boolean lowerCaseTerms = true;

    /**
     * Maximum edit distance for doing lookups. (default 2.0)
     */
    @Builder.Default
    private final double maxEditDistance = 2;

    /**
     * The length of word prefixes used for spell checking. (default 7)
     */
    @Builder.Default
    private final int prefixLength = 10;

    /**
     * The minimum frequency count for dictionary words to be considered correct spellings. (default
     * 1)
     */
    @Builder.Default
    private final long countThreshold = 1;
    /**
     * Ignore the word in resultset, if suggestions are empty
     */
    @Builder.Default
    private final boolean ignoreUnknown = true;
    /**
     * Edit Factor to compute max edit distance possible for a word.
     */
    @Builder.Default
    private final double editFactor = 0.3;
    @Builder.Default
    private final boolean doKeySplit = true;
    @Builder.Default
    private final String keySplitRegex = "\\s+";
    /**
     * Max keywordLength;
     */
    @Setter
    @Builder.Default
    private int maxLength = Integer.MAX_VALUE;
    @Setter
    @Builder.Default
    private double bigramCountMin = Double.MAX_VALUE;
}
