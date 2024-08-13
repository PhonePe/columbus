package com.phonepe.commons.columbus.search.algorithms.tokenfilter;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.phonepe.commons.columbus.search.algorithms.models.Token;

import java.util.List;

/**
 * Token filters receive a list of tokens and may add, remove, or change tokens.
 * <p>
 * Multiple token filters can be chained in succession to apply a pipeline of transformations
 * to a tokenized text query.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
public interface TokenFilter {

    List<Token> filter(final List<Token> tokens);
}
