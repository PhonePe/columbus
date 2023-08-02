package com.phonepe.platform.columbus.common.pair;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PairFunctionsTest {

    private static final Map<String, Integer> SAMPLE_ENTRIES;
    private static final List<Pair<String, Integer>> SAMPLE_PAIRS;

    @Test
    @DisplayName("combine pair values")
    void testCombine() {
        final var expected = SAMPLE_ENTRIES.entrySet().stream()
                .map(entry -> entry.getKey().repeat(entry.getValue()))
                .sorted()
                .toList();

        final var actual = SAMPLE_PAIRS.stream()
                .map(PairFunctions.combine(String::repeat))
                .sorted()
                .toList();

        assertEquals(expected, actual, "combinations should match");
    }

    @Test
    @DisplayName("consume pair values")
    void testConsume() {
        final var map = new HashMap<String, Integer>();

        SAMPLE_PAIRS.forEach(PairFunctions.consume(map::put));

        assertEquals(SAMPLE_ENTRIES, map, "pairs should retain all entries");
    }

    @Test
    @DisplayName("transform pair values")
    void testTransform() {
        final var expected = SAMPLE_ENTRIES.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toLowerCase(),
                        entry -> entry.getValue() * 2
                ));

        final var actual = SAMPLE_PAIRS.stream()
                .map(PairFunctions.transforming(String::toLowerCase, integer -> integer * 2))
                .collect(Collectors.toMap(Pair::first, Pair::second));

        assertEquals(expected, actual, "transformations should match");
    }

    @Test
    @DisplayName("filter by values")
    void testFilter() {
        final var expected = SAMPLE_ENTRIES.entrySet().stream()
                .filter(entry -> entry.getValue() == entry.getKey().length())
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

        final var actual = SAMPLE_PAIRS.stream()
                .filter(PairFunctions.filtering((first, second) -> second == first.length()))
                .collect(Collectors.toMap(Pair::first, Pair::second));

        assertEquals(expected, actual, "filtered values should match");
    }

    @Nested
    @DisplayName("tests modifying first value")
    class FirstValueTest {

        private static final Function<String, String> KEY_MODIFIER = String::toLowerCase;
        private static final Predicate<String> KEY_TEST = string -> string.length() > 4;
        private static final List<String> MODIFIED_KEYS = SAMPLE_ENTRIES.keySet().stream()
                .map(KEY_MODIFIER)
                .sorted()
                .toList();
        private static final List<Integer> FILTERED_VALUES = SAMPLE_ENTRIES.entrySet().stream()
                .filter(entry -> KEY_TEST.test(entry.getKey()))
                .map(Entry::getValue)
                .sorted()
                .toList();

        @Test
        @DisplayName("transform first value")
        void testTransform() {
            final var keys = SAMPLE_PAIRS.stream()
                    .map(PairFunctions.transformingFirst(KEY_MODIFIER))
                    .map(Pair::first)
                    .sorted()
                    .toList();

            assertEquals(MODIFIED_KEYS, keys, "all keys should be modified");
        }

        @Test
        @DisplayName("map first value")
        void testMap() {
            final var keys = SAMPLE_PAIRS.stream()
                    .map(PairFunctions.mapFirst(KEY_MODIFIER))
                    .sorted()
                    .toList();

            assertEquals(MODIFIED_KEYS, keys, "all keys should be modified");
        }

        @Test
        @DisplayName("filter by first value")
        void testFilter() {
            final var values = SAMPLE_PAIRS.stream()
                    .filter(PairFunctions.filteringFirst(KEY_TEST))
                    .map(Pair::second)
                    .sorted()
                    .toList();

            assertEquals(FILTERED_VALUES, values, "only values with long keys should be retained");
        }
    }

    @Nested
    @DisplayName("tests modifying second value")
    class SecondValueTest {

        private static final Function<Integer, Integer> VALUE_MODIFIER = integer -> integer + integer;
        private static final Predicate<Integer> VALUE_TEST = integer -> integer % 2 == 0;
        private static final List<Integer> MODIFIED_VALUES = SAMPLE_ENTRIES.values().stream()
                .map(VALUE_MODIFIER)
                .sorted()
                .toList();
        private static final List<String> FILTERED_KEYS = SAMPLE_ENTRIES.entrySet().stream()
                .filter(entry -> VALUE_TEST.test(entry.getValue()))
                .map(Entry::getKey)
                .sorted()
                .toList();

        @Test
        @DisplayName("transform second value")
        void testTransform() {
            final var values = SAMPLE_PAIRS.stream()
                    .map(PairFunctions.transformingSecond(VALUE_MODIFIER))
                    .map(Pair::second)
                    .sorted()
                    .toList();

            assertEquals(MODIFIED_VALUES, values, "all values should be doubled");
        }

        @Test
        @DisplayName("map second value")
        void testMap() {
            final var values = SAMPLE_PAIRS.stream()
                    .map(PairFunctions.mapSecond(VALUE_MODIFIER))
                    .sorted()
                    .toList();

            assertEquals(MODIFIED_VALUES, values, "all values should be doubled");
        }

        @Test
        @DisplayName("filter by second value")
        void testFilter() {
            final var values = SAMPLE_PAIRS.stream()
                    .filter(PairFunctions.filteringSecond(VALUE_TEST))
                    .map(Pair::first)
                    .sorted()
                    .toList();

            assertEquals(FILTERED_KEYS, values, "only keys with even values should be retained");
        }
    }

    static {
        SAMPLE_ENTRIES = Map.of(
                "Some", 4,
                "samPlE", 5,
                "PAIR", 4,
                "Values", 3,
                "for", 3,
                "Testing", 4
        );
        SAMPLE_PAIRS = SAMPLE_ENTRIES.entrySet().stream()
                .map(entry -> Pair.of(entry.getKey(), entry.getValue()))
                .toList();
    }
}
