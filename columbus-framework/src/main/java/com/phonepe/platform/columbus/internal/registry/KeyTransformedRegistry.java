package com.phonepe.platform.columbus.internal.registry;

import com.phonepe.platform.columbus.registry.Registry;
import lombok.Builder;
import lombok.NonNull;

import java.util.Optional;
import java.util.function.Function;

/**
 * A registry that first transforms the given key and then uses the resulting key to fetch the value from the delegate.
 *
 * @param <K> the type of the key.
 * @param <T> the type of the transform result, which acts as the key to {@link #registry}.
 * @param <V> the type of the values.
 */
@Builder
public class KeyTransformedRegistry<K, T, V> implements Registry<K, V> {

    @NonNull
    private final Function<? super K, ? extends T> keyTransformer;

    @NonNull
    private final Registry<? super T, ? extends V> registry;

    @Override
    public Optional<? extends V> get(final K key) {
        return registry.get(keyTransformer.apply(key));
    }
}
