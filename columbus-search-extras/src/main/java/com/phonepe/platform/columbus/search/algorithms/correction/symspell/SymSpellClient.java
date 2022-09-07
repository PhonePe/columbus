package com.phonepe.platform.columbus.search.algorithms.correction.symspell;

import com.phonepe.platform.columbus.core.exception.ColumbusException;
import com.phonepe.platform.columbus.search.algorithms.correction.Correction;
import com.phonepe.platform.columbus.search.algorithms.correction.CorrectionDataManager;
import com.phonepe.platform.columbus.search.algorithms.correction.Suggestion;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Builder
@AllArgsConstructor
public class SymSpellClient implements Correction, CorrectionDataManager {
    private final SymSpellCorrector symSpellCorrector;
    private final InMemoryDataHolder inMemoryDataHolder;

    @Override
    public void add(String word, double freq) throws ColumbusException {
        this.inMemoryDataHolder.addItem(word, freq);
    }

    @Override
    public void clear() {
        this.inMemoryDataHolder.clear();
    }

    @Override
    public List<String> process(String input, int maxResults) throws ColumbusException {
        List<Suggestion> res = symSpellCorrector.correctQuery(input, 2.0);
        return res.stream().limit(maxResults).map(Suggestion::getTerm).toList();

    }
}
