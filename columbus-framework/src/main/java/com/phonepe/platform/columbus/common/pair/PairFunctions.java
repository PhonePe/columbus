package com.phonepe.platform.columbus.common.pair;

import lombok.experimental.UtilityClass;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * This class consists exclusively of functional interfaces that operate on {@link Pair}s. The methods are defined to
 * help use {@link Pair}s in {@link java.util.stream.Stream}s and other such functional usages.
 */
@UtilityClass
public class PairFunctions {

    public <F, S, T> Function<Pair<F, S>, T> combine(final BiFunction<F, S, T> combiner) {
        return pair -> pair.combine(combiner);
    }

    public <F, S> Consumer<Pair<F, S>> consume(final BiConsumer<F, S> consumer) {
        return pair -> consumer.accept(pair.first(), pair.second());
    }

    public <F, S, R, T> Function<Pair<F, S>, Pair<R, T>> transforming(
            final Function<F, R> firstFn,
            final Function<S, T> secondFn
    ) {
        return pair -> pair.transform(firstFn, secondFn);
    }

    public <F, S, T> Function<Pair<F, S>, Pair<T, S>> transformingFirst(final Function<F, T> function) {
        return pair -> pair.transformFirst(function);
    }

    public <F, S, T> Function<Pair<F, S>, Pair<F, T>> transformingSecond(final Function<S, T> function) {
        return pair -> pair.transformSecond(function);
    }

    public <F, S, T> Function<Pair<F, S>, T> mapFirst(final Function<F, T> function) {
        return function.compose(Pair::first);
    }

    public <F, S, T> Function<Pair<F, S>, T> mapSecond(final Function<S, T> function) {
        return function.compose(Pair::second);
    }

    public <F, S> Predicate<Pair<F, S>> filtering(final BiPredicate<F, S> predicate) {
        return pair -> predicate.test(pair.first(), pair.second());
    }

    public <F, S> Predicate<Pair<F, S>> filteringFirst(final Predicate<F> predicate) {
        return pair -> predicate.test(pair.first());
    }

    public <F, S> Predicate<Pair<F, S>> filteringSecond(final Predicate<S> predicate) {
        return pair -> predicate.test(pair.second());
    }
}
