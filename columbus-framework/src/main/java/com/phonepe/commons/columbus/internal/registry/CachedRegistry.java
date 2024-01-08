package com.phonepe.commons.columbus.internal.registry;

import com.phonepe.commons.columbus.registry.Registry;
import lombok.Builder;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A registry that caches the results from the delegate.
 * <p>
 * For any new key, the result is fetched from the {@link CachedRegistry#delegate}, and stored for all future gets.
 *
 * @param <K> the type of the key.
 * @param <V> the type of the values.
 */
@Builder
public class CachedRegistry<K, V> implements Registry<K, V> {

    @NonNull
    @Builder.Default
    private final Map<K, Optional<? extends V>> entries = new HashMap<>();

    @NonNull
    private final Registry<? super K, ? extends V> delegate;

    @Override
    public Optional<? extends V> get(final K key) {
        return entries.computeIfAbsent(key, delegate::get);
    }
}
