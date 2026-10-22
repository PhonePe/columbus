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

import com.phonepe.columbus.search.algorithms.models.Token;
import com.phonepe.columbus.search.algorithms.tokenize.impl.WhitespaceTokenizer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TokenizerTest {

    @Nested
    class RegexSplit {

        @Test
        @DisplayName("whitespace without limit")
        void testWhitespace() {
            final var text = " \ta  \n b\t ";
            final var expected = List.of(
                    Token.builder()
                            .text("a")
                            .build(),
                    Token.builder()
                            .text("b")
                            .tail(true)
                            .build()
            );

            final var actual = WhitespaceTokenizer.builder().build().tokenize(text);

            assertEquals(expected, actual, "tokens not split by whitespace");
        }

        @Test
        @DisplayName("whitespace with tokenizer limit")
        void testWhitespaceWithTokenizerLimit() {
            final var text = " a  \n b\t c";
            final var expected = List.of(
                    Token.builder()
                            .text("a")
                            .build(),
                    Token.builder()
                            .text("b")
                            .build()
            );

            final var actual = WhitespaceTokenizer.builder().limit(2).build().tokenize(text);

            assertEquals(expected, actual, "tokenizer limit not applied");
        }

        @Test
        @DisplayName("whitespace with param limit")
        void testWhitespaceWithParamLimit() {
            final var text = " a  \n b\t c";
            final var expected = List.of(
                    Token.builder()
                            .text("a")
                            .build(),
                    Token.builder()
                            .text("b")
                            .build()
            );

            final var actual = WhitespaceTokenizer.builder().build().tokenize(text, 2);

            assertEquals(expected, actual, "token limit param not applied");
        }
    }
}
