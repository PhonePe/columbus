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
package com.phonepe.columbus.search.algorithms.tokenfilter.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.columbus.search.algorithms.models.Token;
import com.phonepe.columbus.search.algorithms.tokenfilter.TokenFilter;
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
