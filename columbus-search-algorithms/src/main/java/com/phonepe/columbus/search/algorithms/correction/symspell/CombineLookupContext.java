package com.phonepe.columbus.search.algorithms.correction.symspell;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CombineLookupContext {

    private String token;
    private String previousToken;
    private double maxEditDistance;
}
