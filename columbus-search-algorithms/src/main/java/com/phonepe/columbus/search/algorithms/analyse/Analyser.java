/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
