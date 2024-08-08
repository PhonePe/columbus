package com.phonepe.commons.columbus.search.algorithms.correction;

public interface CorrectionDataManager {

    void add(String word, double freq);

    void clear();
}
