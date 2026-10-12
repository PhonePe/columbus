package com.phonepe.columbus.common;

import lombok.experimental.UtilityClass;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.function.Predicate;

/**
 * A collection of utilities that involve conditional execution of some code.
 */
@UtilityClass
public class Predicates {

    public static <T> void applyIf(final T value, final Predicate<T> condition, final Consumer<T> consumer) {
        if (condition.test(value)) {
            consumer.accept(value);
        }
    }

    public static <T> void applyIfNotNull(@Nullable final T value, final Consumer<T> consumer) {
        if (value != null) {
            consumer.accept(value);
        }
    }

    public static void applyIfPositive(@Nullable final Integer value, final IntConsumer consumer) {
        if (value != null && value > 0) {
            consumer.accept(value);
        }
    }

    public static void applyIfNonNegative(@Nullable final Integer value, final IntConsumer consumer) {
        if (value != null && value >= 0) {
            consumer.accept(value);
        }
    }

    public static void applyIfNonNegative(@Nullable final Long value, final LongConsumer consumer) {
        if (value != null && value >= 0) {
            consumer.accept(value);
        }
    }

    public static <T, R> void applyIfNotNull(
            @Nullable final T value,
            final Function<T, R> transformer,
            final Consumer<R> consumer
    ) {
        if (value != null) {
            applyIfNotNull(transformer.apply(value), consumer);
        }
    }
}
