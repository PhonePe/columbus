package com.phonepe.commons.columbus.internal.registry;

import com.phonepe.commons.columbus.registry.Registry;
import lombok.Builder;
import lombok.NonNull;

import java.util.Optional;
import java.util.function.Function;

/**
 * A registry that fetches the results from the delegate and then transforms it.
 *
 * @param <K> the type of the key.
 * @param <R> the type of the values in the {@link #registry}.
 * @param <V> the type of the values after transformation.
 */
@Builder
public class ValueTransformedRegistry<K, R, V> implements Registry<K, V> {

    @NonNull
    private final Registry<? super K, ? extends R> registry;

    @NonNull
    private final Function<? super R, ? extends V> valueTransformer;

    @Override
    public Optional<? extends V> get(final K key) {
        return registry.get(key).map(valueTransformer);
    }
}
