package com.phonepe.platform.columbus.search.algorithms.correction.symspell;

import com.phonepe.platform.columbus.search.algorithms.correction.Verbosity;
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
