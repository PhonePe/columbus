package com.phonepe.columbus.internal.registry;

import com.phonepe.columbus.registry.Registry;
import lombok.Builder;
import lombok.NonNull;

import java.util.Optional;

/**
 * A registry that allows monitoring the successes and failures of get operations.
 * <p>
 * The {@link Listener}'s {@link MonitoredRegistry.Listener#success(Object, Object) success(key, value)} is called if a
 * value is found for the key, and {@link Listener#failure(Object) failure(key)} is called if no value is found.
 *
 * @param <K> the type of the key.
 * @param <V> the type of the values.
 */
@Builder
public class MonitoredRegistry<K, V> implements Registry<K, V> {

    @NonNull
    private final Registry<? super K, ? extends V> delegate;

    @NonNull
    private final Listener<? super K, ? super V> listener;

    @Override
    public Optional<? extends V> get(final K key) {
        final var value = delegate.get(key);
        if (value.isPresent()) {
            listener.success(key, value.get());
        } else {
            listener.failure(key);
        }
        return value;
    }

    public interface Listener<K, V> {

        void success(K key, V value);

        void failure(K key);
    }
}
