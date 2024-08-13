package com.phonepe.commons.columbus.search.algorithms.tokenize.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@SuperBuilder
@JsonTypeName(WhitespaceTokenizer.TYPE)
public class WhitespaceTokenizer extends RegexSplitTokenizer {

    public static final String TYPE = "WHITESPACE";

    private static final String SPLITTER = "\\s+";

    protected String splitter() {
        return SPLITTER;
    }
}
