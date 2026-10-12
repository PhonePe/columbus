package com.phonepe.columbus.search.algorithms.tokenize.impl;

import com.phonepe.columbus.search.algorithms.models.Token;
import com.phonepe.columbus.search.algorithms.tokenize.AbstractTokenizer;
import lombok.experimental.SuperBuilder;

import java.util.Arrays;
import java.util.List;

@SuperBuilder
public abstract class RegexSplitTokenizer extends AbstractTokenizer {

    @Override
    public final List<Token> tokenize(final String text, final int limit) {
        final var splits = text.strip().split(splitter(), limit + 1);
        final var tokens = Arrays.stream(splits)
                .limit(limit)
                .map(token -> Token.builder()
                        .text(token)
                        .build())
                .toList();

        if (splits.length <= limit) {
            tokens.get(tokens.size() - 1).setTail(true);
        }

        return tokens;
    }

    protected abstract String splitter();
}
