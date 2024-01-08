package com.phonepe.commons.columbus.internal.registry;

import com.phonepe.commons.columbus.registry.MutableRegistry;
import lombok.Builder;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A registry that caches the results from the delegate, and allows updating the values.
 * <p>
 * For any new key, the result is fetched from the {@link MutableCachedRegistry#delegate}, and stored for future
 * gets, until a new value is registered for a key.
 *
 * @param <K> the type of the key.
 * @param <V> the type of the values.
 */
@Builder
public class MutableCachedRegistry<K, V> implements MutableRegistry<K, V> {

    @NonNull
    @Builder.Default
    private final Map<K, Optional<? extends V>> entries = new HashMap<>();

    @NonNull
    private final MutableRegistry<K, V> delegate;

    @Override
    public Optional<? extends V> get(final K key) {
        return entries.computeIfAbsent(key, delegate::get);
    }

    @Override
    public void register(final K key, final V value) {
        delegate.register(key, value);
        entries.remove(key);
    }
}
