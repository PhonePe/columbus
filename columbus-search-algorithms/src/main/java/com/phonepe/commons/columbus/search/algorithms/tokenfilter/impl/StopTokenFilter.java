package com.phonepe.commons.columbus.search.algorithms.tokenfilter.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.algorithms.models.Token;
import com.phonepe.commons.columbus.search.algorithms.tokenfilter.TokenFilter;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.Set;

@Builder
@Jacksonized
@JsonTypeName(StopTokenFilter.TYPE)
public class StopTokenFilter implements TokenFilter {

    public static final String TYPE = "STOP";

    private final Set<String> stopTokens;

    @Override
    public List<Token> filter(final List<Token> tokens) {
        return tokens.stream()
                .filter(token -> token.isTail() || !stopTokens.contains(token.getText()))
                .toList();
    }
}
