package com.phonepe.columbus.search.algorithms.analyse.impl;

import com.phonepe.columbus.common.Elements;
import com.phonepe.columbus.search.algorithms.analyse.Analyser;
import com.phonepe.columbus.search.algorithms.common.Constants;
import com.phonepe.columbus.search.algorithms.models.Token;
import com.phonepe.columbus.search.algorithms.tokenfilter.TokenFilter;
import com.phonepe.columbus.search.algorithms.tokenize.Tokenizer;
import com.phonepe.columbus.search.algorithms.tokenize.impl.WhitespaceTokenizer;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

@Builder
@Jacksonized
@SuppressWarnings("java:S1170") // Sonar does not identify Lombok's builder default
public final class AnalyserImpl implements Analyser {

    private static final Tokenizer DEFAULT_TOKENIZER = WhitespaceTokenizer.builder().build();

    @NonNull
    @Builder.Default
    private final Tokenizer tokenizer = DEFAULT_TOKENIZER;

    @Singular(ignoreNullCollections = true)
    private final List<@NonNull TokenFilter> filters;

    @Override
    public List<Token> analyse(final String text, final Map<String, Object> params) {
        if (StringUtils.isBlank(text)) {
            return List.of();
        }

        final var tokenizerLimit = Elements.getOrDefault(params, Constants.TOKENIZER_LIMIT, Integer.class);
        var tokens = tokenizerLimit != null ? tokenizer.tokenize(text, tokenizerLimit) : tokenizer.tokenize(text);

        for (final TokenFilter filter : filters) {
            tokens = filter.filter(tokens);
        }

        return tokens;
    }
}
