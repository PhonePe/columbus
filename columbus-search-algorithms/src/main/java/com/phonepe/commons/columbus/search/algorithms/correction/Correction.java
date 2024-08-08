package com.phonepe.commons.columbus.search.algorithms.correction;

import com.phonepe.commons.columbus.common.Elements;

import java.util.List;

public interface Correction {

    default String process(String input) {
        return Elements.getOrDefault(process(input, 1), 0, input);
    }

    List<String> process(String input, int maxResults);
}
