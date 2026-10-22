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
