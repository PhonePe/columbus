package com.phonepe.columbus.internal.registry;

import com.phonepe.columbus.registry.MutableRegistry;
import lombok.Builder;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A mutable registry that uses a {@link HashMap} to store all the key - value pairs.
 *
 * @param <K> the type of the key.
 * @param <V> the type of the values.
 */
@Builder
public class StoredRegistry<K, V> implements MutableRegistry<K, V> {

    @NonNull
    @Builder.Default
    private final Map<K, V> entries = new HashMap<>();

    @Override
    public Optional<? extends V> get(final K key) {
        return Optional.ofNullable(entries.get(key));
    }

    @Override
    public void register(final K key, final V value) {
        entries.put(key, value);
    }
}
