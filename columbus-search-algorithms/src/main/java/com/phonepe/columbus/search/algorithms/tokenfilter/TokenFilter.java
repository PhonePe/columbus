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
package com.phonepe.columbus.search.algorithms.tokenfilter;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.phonepe.columbus.search.algorithms.models.Token;

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
