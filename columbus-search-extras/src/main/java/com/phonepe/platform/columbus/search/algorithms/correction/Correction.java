package com.phonepe.platform.columbus.search.algorithms.correction;


import com.phonepe.platform.columbus.core.exception.ColumbusException;

import java.util.List;

public interface Correction {
    default String process(String input) throws ColumbusException {
        return process(input, 1).stream().findFirst().orElse(input);
    }

    List<String> process(String input, int maxResults) throws ColumbusException;
}
