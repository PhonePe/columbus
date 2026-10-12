package com.phonepe.columbus.search.algorithms.analyse;

import com.phonepe.columbus.search.algorithms.models.Token;

import java.util.List;
import java.util.Map;

/**
 * A collection of the building blocks that make up a text processing pipeline.
 * <p>
 * Facilitates the conversion of unstructured text into a structured format that’s optimized for search.
 * <p>
 * Analysers consist of:
 * <ul>
 *     <li>A tokenizer, to convert the text into a list of tokens</li>
 *     <li>A list of token filters, which may add, remove, or change tokens</li>
 * </ul>
 */
public interface Analyser {

    List<Token> analyse(final String text, final Map<String, Object> params);
}
