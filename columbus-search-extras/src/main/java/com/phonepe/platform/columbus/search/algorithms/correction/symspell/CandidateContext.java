package com.phonepe.platform.columbus.search.algorithms.correction.symspell;

import com.phonepe.platform.columbus.search.algorithms.correction.Verbosity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CandidateContext {
    private String candidate;
    private double maxEditDistance;
    private String phrase;
    private double bestEditDistanceFound;
    private Verbosity verbosity;
    private int phrasePrefixLen;
}
