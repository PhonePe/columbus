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

import com.phonepe.columbus.search.algorithms.models.Token;
import com.phonepe.columbus.search.algorithms.tokenfilter.impl.StopTokenFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TokenFilterTest {

    private static final Token TOKEN1 = Token.builder().text("the").build();
    private static final Token TOKEN2 = Token.builder().text("potato").build();
    private static final Token TOKEN3 = Token.builder().text("of").tail(true).build();

    private static final List<Token> TOKENS = List.of(TOKEN1, TOKEN2, TOKEN3);

    @Test
    @DisplayName("stop token filter")
    void testStopTokenFilter() {
        final var filter = StopTokenFilter.builder()
                .stopTokens(Set.of("the", "of"))
                .build();

        final var expected = List.of(TOKEN2, TOKEN3);
        final var actual = filter.filter(TOKENS);

        assertEquals(expected, actual, "must filter stop tokens");
    }
}
