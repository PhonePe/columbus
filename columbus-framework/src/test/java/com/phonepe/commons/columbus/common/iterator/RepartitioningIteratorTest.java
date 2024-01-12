package com.phonepe.commons.columbus.common.iterator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RepartitioningIteratorTest {

    private static final List<List<Integer>> LIST = List.of(List.of(1, 2, 3), List.of(4, 5), List.of(6));

    @Nested
    class IteratorTest {

        @Test
        @DisplayName("no value")
        void testPartitionEmpty() {
            final var iterator = Iterators.repartitioning(Collections.emptyIterator(), 10);

            assertFalse(iterator.hasNext(), "no elements to partition");
            assertThrows(NoSuchElementException.class, iterator::next, "next() throws if no element left");
        }

        @Test
        @DisplayName("equal partitions")
        void testPartitionEqual() {
            final var iterator = Iterators.repartitioning(LIST.iterator(), 3);

            assertTrue(iterator.hasNext(), "6 elements present in partition");
            assertEquals(List.of(1, 2, 3), iterator.next(), "first partition has 3 elements");
            assertTrue(iterator.hasNext(), "3 elements present in partition");
            assertEquals(List.of(4, 5, 6), iterator.next(), "second partition has 3 elements");
            assertFalse(iterator.hasNext(), "no elements left in partition");
            assertThrows(NoSuchElementException.class, iterator::next, "next() throws if no element left");
        }

        @Test
        @DisplayName("unequal partitions")
        void testPartitionUnequal() {
            final var iterator = Iterators.repartitioning(LIST.iterator(), 4);

            assertTrue(iterator.hasNext(), "6 elements present in partition");
            assertEquals(List.of(1, 2, 3, 4), iterator.next(), "first partition has 4 elements");
            assertTrue(iterator.hasNext(), "2 elements present in partition");
            assertEquals(List.of(5, 6), iterator.next(), "second partition has 2 elements");
            assertFalse(iterator.hasNext(), "no elements left in partition");
            assertThrows(NoSuchElementException.class, iterator::next, "next() throws if no element left");
        }

        @Test
        @DisplayName("invalid size")
        void testInvalidSize() {
            final var iterator = LIST.iterator();
            assertAll(
                    "invalid sizes",
                    () -> assertThrows(
                            IllegalArgumentException.class,
                            () -> Iterators.repartitioning(iterator, 0),
                            "zero size must throw"),
                    () -> assertThrows(
                            IllegalArgumentException.class,
                            () -> Iterators.repartitioning(iterator, -1),
                            "negative size must throw"));
        }
    }

    @Nested
    class StreamTest {

        @Test
        @DisplayName("no value")
        void testPartitionEmpty() {
            final var result = Iterators.repartitioning(Stream.of(), 10).toList();

            assertTrue(result.isEmpty(), "no elements to partition");
        }

        @Test
        @DisplayName("equal partitions")
        void testPartitionEqual() {
            final var result = Iterators.repartitioning(LIST.stream(), 3).toList();

            assertEquals(List.of(List.of(1, 2, 3), List.of(4, 5, 6)), result, "2 equal partitions returned");
        }

        @Test
        @DisplayName("unequal partitions")
        void testPartitionUnequal() {
            final var result = Iterators.repartitioning(LIST.stream(), 4).toList();

            assertEquals(List.of(List.of(1, 2, 3, 4), List.of(5, 6)), result, "2 unequal partitions returned");
        }

        @Test
        @DisplayName("invalid size")
        void testInvalidSize() {
            final var stream0 = LIST.stream();
            final var stream1 = LIST.stream();
            assertAll(
                    "invalid sizes",
                    () -> assertThrows(
                            IllegalArgumentException.class,
                            () -> Iterators.repartitioning(stream0, 0),
                            "zero size must throw"),
                    () -> assertThrows(
                            IllegalArgumentException.class,
                            () -> Iterators.repartitioning(stream1, -1),
                            "negative size must throw"));
        }
    }
}
