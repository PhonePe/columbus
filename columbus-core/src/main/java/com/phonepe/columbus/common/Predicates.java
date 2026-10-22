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
