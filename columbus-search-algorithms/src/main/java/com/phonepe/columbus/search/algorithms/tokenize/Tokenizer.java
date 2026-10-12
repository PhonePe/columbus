package com.phonepe.columbus.search.algorithms.tokenize;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.phonepe.columbus.search.algorithms.models.Token;

import java.util.List;

/**
 * An interface for the different tokenizing algorithms that can be applied to a text query.
 * <p>
 * Tokenizing consists of breaking up text into a list of tokens (usually individual words),
 * generally used as one processing step as part of a text search/ingestion pipeline.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
public interface Tokenizer {

    /**
     * @param limit max number of tokens to be generated
     * @return list of generated tokens
     */
    List<Token> tokenize(final String text, final int limit);

    List<Token> tokenize(final String text);
}
