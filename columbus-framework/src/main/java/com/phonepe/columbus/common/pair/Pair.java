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
package com.phonepe.columbus.common.pair;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * An object representing two values of types {@code F} and {@code S}. The object in itself is immutable, but
 * provides various helper methods to easily operate on the data, and create instances with updated values.
 *
 * @param <F> the type of first value.
 * @param <S> the type of second value.
 */
@Data
@Builder
@Jacksonized
@Accessors(fluent = true)
@AllArgsConstructor(staticName = "of")
public class Pair<F, S> {

    @With
    private final F first;
    @With
    private final S second;

    /**
     * Creates a new pair by transforming both the values.
     *
     * @param firstFn  the transformation to be applied to {@link Pair#first}.
     * @param secondFn the transformation to be applied to {@link Pair#second}.
     * @param <R>      the new type for {@link Pair#first}.
     * @param <T>      the new type for {@link Pair#second}.
     * @return a pair with the new values.
     */
    public <R, T> Pair<R, T> transform(final Function<F, R> firstFn, final Function<S, T> secondFn) {
        return of(firstFn.apply(first), secondFn.apply(second));
    }

    /**
     * Creates a new pair by transforming the {@link Pair#first} value.
     *
     * @param function the transformation to be applied.
     * @param <T>      the new type for {@link Pair#first}.
     * @return a pair with the new values.
     */
    public <T> Pair<T, S> transformFirst(final Function<F, T> function) {
        return of(function.apply(first), second);
    }

    /**
     * Creates a new pair by transforming the {@link Pair#second} value.
     *
     * @param function the transformation to be applied.
     * @param <T>      the new type for {@link Pair#second}.
     * @return a pair with the new values.
     */
    public <T> Pair<F, T> transformSecond(final Function<S, T> function) {
        return of(first, function.apply(second));
    }

    /**
     * Combines the two values of the pair by applying {@link BiFunction#apply(Object, Object) combiner.apply(F, S)}
     * with the {@link Pair#first} and {@link Pair#second} values.
     *
     * @param combiner the function to be applied with the values of the pair.
     * @param <T>      the type of the result of the combination.
     * @return the result of the combination.
     */
    public <T> T combine(final BiFunction<F, S, T> combiner) {
        return combiner.apply(first, second);
    }
}
