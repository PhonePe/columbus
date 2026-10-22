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
package com.phonepe.columbus.search.algorithms.analyse.impl;

import com.phonepe.columbus.search.algorithms.analyse.Analyser;
import com.phonepe.columbus.search.algorithms.common.Constants;
import com.phonepe.columbus.search.algorithms.models.Token;
import com.phonepe.columbus.search.algorithms.tokenfilter.TokenFilter;
import com.phonepe.columbus.search.algorithms.tokenize.Tokenizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class AnalyserImplTest {

    private static final String TEXT = "a,b c-d";

    private static final Token TOKEN1 = Token.builder().text("a").build();
    private static final Token TOKEN2 = Token.builder().text("b").build();
    private static final Token TOKEN3 = Token.builder().text("c").tail(true).build();

    private static final List<Token> TOKENS_1_2_3 = List.of(TOKEN1, TOKEN2, TOKEN3);
    private static final List<Token> TOKENS_1_3 = List.of(TOKEN1, TOKEN3);
    private static final List<Token> TOKENS_3 = List.of(TOKEN3);

    @Mock
    private Tokenizer tokenizer;

    @Mock
    private TokenFilter tokenFilter1;
    @Mock
    private TokenFilter tokenFilter2;

    private Analyser analyser;

    @BeforeEach
    void setUp() {
        analyser = AnalyserImpl.builder()
                .tokenizer(tokenizer)
                .filter(tokenFilter1)
                .filter(tokenFilter2)
                .build();
    }

    @Test
    @DisplayName("analyse text without limit")
    void testAnalyseWithoutLimit() {
        doReturn(TOKENS_1_2_3).when(tokenizer).tokenize(TEXT);

        doReturn(TOKENS_1_3).when(tokenFilter1).filter(TOKENS_1_2_3);
        doReturn(TOKENS_3).when(tokenFilter2).filter(TOKENS_1_3);

        final var actual = analyser.analyse(TEXT, null);

        assertEquals(TOKENS_3, actual, "tokens must match");
    }

    @Test
    @DisplayName("analyse text with limit")
    void testAnalyseLimit() {
        doReturn(TOKENS_1_2_3).when(tokenizer).tokenize(TEXT, 3);

        doReturn(TOKENS_1_3).when(tokenFilter1).filter(TOKENS_1_2_3);
        doReturn(TOKENS_3).when(tokenFilter2).filter(TOKENS_1_3);

        final var actual = analyser.analyse(TEXT, Map.of(Constants.TOKENIZER_LIMIT, 3));

        assertEquals(TOKENS_3, actual, "tokens must match");
    }

    @Test
    @DisplayName("analyse text with invalid limit")
    void testAnalyseInvalidLimit() {
        doReturn(TOKENS_1_2_3).when(tokenizer).tokenize(TEXT);

        doReturn(TOKENS_1_3).when(tokenFilter1).filter(TOKENS_1_2_3);
        doReturn(TOKENS_3).when(tokenFilter2).filter(TOKENS_1_3);

        final var actual = analyser.analyse(TEXT, Map.of(Constants.TOKENIZER_LIMIT, 3L));

        assertEquals(TOKENS_3, actual, "tokens must match");
    }

    @Test
    @DisplayName("analyse null text")
    void testAnalyseNull() {
        final var actual = analyser.analyse(null, Map.of());

        assertTrue(actual.isEmpty(), "tokens must be empty");
    }
}
