package com.phonepe.commons.columbus.search.algorithms.correction.symspell;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SuggestionContext {

    private String phrase;
    private double maxEditDistance;
    private String suggestion;
    private String candidate;
    private Verbosity verbosity;
    private int minDistance;
}
