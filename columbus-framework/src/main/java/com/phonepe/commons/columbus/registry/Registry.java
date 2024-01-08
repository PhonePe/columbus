package com.phonepe.commons.columbus.registry;

import java.util.Optional;

/**
 * An object that provides a way to register some values against some unique keys, and fetch them.
 * <p>
 * This is very similar to {@link java.util.Map Map}s, but is focused more on configurability than storage efficiency.
 *
 * @param <K> the type of the key.
 * @param <V> the type of the values.
 */
public interface Registry<K, V> {

    /**
     * Finds the value that is registered for a given key.
     *
     * @param key the key whose value needs to be fetched.
     * @return the value registered for the key, or empty if no value is registered.
     */
    @SuppressWarnings("java:S1452")
    Optional<? extends V> get(K key);
}
