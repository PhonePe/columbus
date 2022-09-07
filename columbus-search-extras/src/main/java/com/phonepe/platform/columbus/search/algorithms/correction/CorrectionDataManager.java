package com.phonepe.platform.columbus.search.algorithms.correction;


import com.phonepe.platform.columbus.core.exception.ColumbusException;

public interface CorrectionDataManager {
    void add(String word, double freq) throws ColumbusException;

    void clear();

}
