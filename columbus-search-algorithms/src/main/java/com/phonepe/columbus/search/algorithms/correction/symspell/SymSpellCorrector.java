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

import com.phonepe.columbus.search.algorithms.common.distance.Distance;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public class SymSpellCorrector {

    private final UnigramCorrector unigramCorrector;
    private final CombineWordsChecker combineWordsChecker;
    private final WordSplitChecker wordSplitChecker;
    private final SymSpellConfig symSpellConfig;

    private final Distance<String> stringDistance;

    public List<Suggestion> correctQuery(String phrase, double maxEditDistance) {
        if (maxEditDistance > symSpellConfig.getMaxEditDistance()) {
            throw new IllegalArgumentException("max Edit distance should be less than  global Max ");
        } else if (phrase.isEmpty()) {
            throw new IllegalArgumentException("Invalid input of string");
        }
        return lookupCompound(phrase, maxEditDistance, true);
    }

    @SuppressWarnings("SameParameterValue")
    private List<Suggestion> lookupCompound(String phrase, double maxEditDistance, boolean tokenizeOnWhiteSpaces) {
        List<String> items = transform(phrase, tokenizeOnWhiteSpaces);
        List<Suggestion> suggestions;
        List<Suggestion> suggestionParts = new ArrayList<>();
        boolean lastTwoWordsCombined = false;
        for (int i = 0; i < items.size(); i++) {
            suggestions = unigramCorrector.correctWord(items.get(i), maxEditDistance);
            if (i > 0 && !lastTwoWordsCombined) {
                CombineLookupContext combineLookupContext = CombineLookupContext.builder()
                        .token(items.get(i))
                        .previousToken(items.get(i - 1))
                        .maxEditDistance(maxEditDistance)
                        .build();
                if (combineWordsChecker.canBeCombined(combineLookupContext, suggestionParts, suggestions)) {
                    lastTwoWordsCombined = true;
                    continue;
                }
            }
            lastTwoWordsCombined = false;
            if (!suggestions.isEmpty() && (suggestions.get(0).getDistance() == 0
                    || items.get(i).length() == 1)) {
                suggestionParts.add(suggestions.get(0));
            } else {
                wordSplitChecker.checkSplitWord(suggestionParts, suggestions, items.get(i), maxEditDistance);
            }
        }
        Suggestion joinedTerm = SymSpellUtils.joinSuggestionParts(suggestionParts);
        double joinedDistance = stringDistance.distance(joinedTerm.getTerm(), phrase, Math.pow(2, 31) - 1);
        joinedTerm.setDistance(joinedDistance);
        return Collections.singletonList(joinedTerm);
    }

    private List<String> transform(String query, final boolean tokenizeOnWhiteSpaces) {
        query = symSpellConfig.isLowerCaseTerms() ? query.toLowerCase() : query;
        if (tokenizeOnWhiteSpaces) {
            return Arrays.stream(query.split("\\s+")).toList();
        }
        return List.of(query);
    }
}
