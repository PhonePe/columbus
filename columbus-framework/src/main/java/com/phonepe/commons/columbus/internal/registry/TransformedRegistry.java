package com.phonepe.commons.columbus.internal.registry;

import com.phonepe.commons.columbus.registry.Registry;
import lombok.Builder;
import lombok.NonNull;

import java.util.Optional;
import java.util.function.Function;

/**
 * A registry that first transforms the given key, uses it to fetch the results, and then transforms it to the
 * desired type.
 *
 * @param <K> the type of the key before transformation.
 * @param <T> the type of the keys in the {@link #registry}.
 * @param <R> the type of the values in the {@link #registry}.
 * @param <V> the type of the values after transformation.
 */
@Builder
public class TransformedRegistry<K, T, R, V> implements Registry<K, V> {

    @NonNull
    private final Function<? super K, ? extends T> keyTransformer;

    @NonNull
    private final Registry<? super T, ? extends R> registry;

    @NonNull
    private final Function<? super R, ? extends V> valueTransformer;

    @Override
    public Optional<? extends V> get(final K key) {
        return registry.get(keyTransformer.apply(key)).map(valueTransformer);
    }
}
