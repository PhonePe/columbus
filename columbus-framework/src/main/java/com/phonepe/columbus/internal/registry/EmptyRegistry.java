package com.phonepe.columbus.internal.registry;

import com.phonepe.columbus.registry.Registry;
import lombok.NoArgsConstructor;

import java.util.Optional;

/**
 * Well, it's empty.
 * <p>
 * Returns empty value for all keys.
 *
 * @param <K> the type of the key.
 * @param <V> the type of the values.
 */
@NoArgsConstructor(staticName = "create")
public class EmptyRegistry<K, V> implements Registry<K, V> {

    @Override
    public Optional<? extends V> get(final K key) {
        return Optional.empty();
    }
}
