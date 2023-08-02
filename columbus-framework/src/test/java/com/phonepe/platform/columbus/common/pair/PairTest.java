package com.phonepe.platform.columbus.common.pair;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PairTest {

    @Test
    @DisplayName("create with constructor")
    void testConstructor() {
        final var pair = Pair.of("Constructor", 11);

        verifyPair(pair, "Constructor", 11);
    }

    @Test
    @DisplayName("create with builder")
    void testBuilder() {
        final var pair = Pair.<String, Integer>builder()
                .first("Builder")
                .second(7)
                .build();

        verifyPair(pair, "Builder", 7);
    }

    @Test
    @DisplayName("immutable field update")
    void testWithField() {
        final var pair = Pair.of("with", "fields");
        final var pair1 = pair.withFirst("first");
        final var pair2 = pair.withSecond("second");

        verifyPair(pair, "with", "fields");
        verifyPair(pair1, "first", "fields");
        verifyPair(pair2, "with", "second");
    }

    @Test
    @DisplayName("transform both values")
    void testTransform() {
        final var pair = Pair.of("First", "Second")
                .transform(String::toLowerCase, String::toUpperCase);

        verifyPair(pair, "first", "SECOND");
    }

    @Test
    @DisplayName("transform first value")
    void testTransformFirst() {
        final var pair = Pair.of("first", "transform")
                .transformFirst(String::length);

        verifyPair(pair, 5, "transform");
    }

    @Test
    @DisplayName("transform second value")
    void testTransformSecond() {
        final var pair = Pair.of("transform", "second")
                .transformSecond(String::length);

        verifyPair(pair, "transform", 6);
    }

    @Test
    @DisplayName("combine values")
    void testCombine() {
        final var combination = Pair.of("combination", 12)
                .combine((first, second) -> first.length() * second);

        assertEquals(132, combination, "combination result should match");
    }

    private static <F, S> void verifyPair(final Pair<F, S> pair, final F first, final S second) {
        assertAll(
                "pair values",
                () -> assertEquals(first, pair.first(), "first value should be equal"),
                () -> assertEquals(second, pair.second(), "second value should be equal")
        );
    }
}
