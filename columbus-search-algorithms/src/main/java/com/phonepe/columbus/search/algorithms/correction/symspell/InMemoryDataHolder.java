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

import lombok.Getter;
import org.apache.commons.codec.digest.MurmurHash3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
public class InMemoryDataHolder {

    private final Map<String, Double> wordsDictionary = new HashMap<>();
    private final Map<String, Double> bigramsDictionary = new HashMap<>();

    /**
     * Dictionary that contains a mapping of lists of suggested correction words to the hashCodes of
     * the original words and the deletes derived from them. Collisions of hashCodes is tolerated,
     * because suggestions are ultimately verified via an edit distance function. A list of
     * suggestions might have a single suggestion, or multiple suggestions.
     */
    private final Map<Long, List<String>> deletes = new HashMap<>();

    /**
     * Spell check settings to use the values while ingesting the terms.
     */
    private final SymSpellConfig symSpellConfig;

    public InMemoryDataHolder(
            SymSpellConfig symSpellConfig
    ) {
        this.symSpellConfig = symSpellConfig;
    }

    public boolean addItem(final String word, final double freq) {
        if (freq <= 0 && symSpellConfig.getCountThreshold() > 0) {
            return false;
        }
        double frequency = freq;
        String key = word;
        if (symSpellConfig.isLowerCaseTerms()) {
            key = key.toLowerCase();
        }
        if (frequency <= 0) {
            frequency = 0;
        }
        //Adding new threshold word
        if (!addToDictionary(key, frequency)) {
            return false;
        }
        /*
         * edits/suggestions are created only once, no matter how often
         * word occurs. edits/suggestions are created as soon as the
         * word occurs in the corpus, even if the same term existed
         * before in the dictionary as an edit from another word
         */
        if (key.length() > symSpellConfig.getMaxLength()) {
            symSpellConfig.setMaxLength(key.length());
        }
        //create deletes
        Set<String> editDeletes = SymSpellUtils
                .getEditDeletes(key, symSpellConfig.getMaxEditDistance(),
                        symSpellConfig.getPrefixLength(), symSpellConfig.getEditFactor());
        for (String delete : editDeletes) {
            Long hash = MurmurHash3.hash64(delete.getBytes());
            if (deletes.containsKey(hash)) {
                deletes.get(hash).add(key);
            } else {
                deletes.put(hash, new ArrayList<>(List.of(key)));
            }
        }
        return true;
    }

    public Double getItemFrequency(String term) {
        return wordsDictionary.getOrDefault(term, null);
    }

    public Double getItemFrequencyBiGram(String term) {
        return bigramsDictionary.getOrDefault(term, null);
    }

    public List<String> getDeletes(String key) {
        return deletes.getOrDefault(MurmurHash3.hash64(key.getBytes()), null);
    }

    public void clear() {
        bigramsDictionary.clear();
        wordsDictionary.clear();
        deletes.clear();
    }

    private boolean addToDictionary(String key, double frequency) {
        if (symSpellConfig.isDoKeySplit()
                && key.split(symSpellConfig.getKeySplitRegex()).length > 1) {
            bigramsDictionary.put(key, frequency);
            if (frequency < symSpellConfig.getBigramCountMin()) {
                symSpellConfig.setBigramCountMin(frequency);
            }
            return false;
        } else if (wordsDictionary.containsKey(key)) {
            return false;
        } else {
            wordsDictionary.put(key, frequency);
            return true;
        }
    }
}
