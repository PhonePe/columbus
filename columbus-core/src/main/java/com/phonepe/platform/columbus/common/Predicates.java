package com.phonepe.platform.columbus.common;

import lombok.experimental.UtilityClass;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A collection of utilities that involve conditional execution of some code.
 */
@UtilityClass
public class Predicates {

    public static <T> void applyIfNotNull(@Nullable final T value, final Consumer<T> consumer) {
        if (value != null) {
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
