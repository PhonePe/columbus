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
