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

import com.phonepe.columbus.search.algorithms.correction.Correction;
import com.phonepe.columbus.search.algorithms.correction.CorrectionDataManager;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Builder
@AllArgsConstructor
public class SymSpellClient implements Correction, CorrectionDataManager {

    private final SymSpellCorrector symSpellCorrector;
    private final InMemoryDataHolder inMemoryDataHolder;

    @Override
    public void add(String word, double freq) {
        this.inMemoryDataHolder.addItem(word, freq);
    }

    @Override
    public void clear() {
        this.inMemoryDataHolder.clear();
    }

    @Override
    public List<String> process(String input, int maxResults) {
        List<Suggestion> res = symSpellCorrector.correctQuery(input, 2.0);
        return res.stream().limit(maxResults).map(Suggestion::getTerm).toList();
    }
}
