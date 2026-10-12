package com.phonepe.columbus.search.algorithms.tokenize;

import com.phonepe.columbus.search.algorithms.models.Token;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Positive;
import java.util.List;

@SuperBuilder
@SuppressWarnings("java:S1170") // Sonar does not identify Lombok's builder default
public abstract class AbstractTokenizer implements Tokenizer {

    @Positive
    @Builder.Default
    private final int limit = Integer.MAX_VALUE;

    @Override
    public final List<Token> tokenize(final String text) {
        return tokenize(text, limit);
    }
}
