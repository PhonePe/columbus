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
