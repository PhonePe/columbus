package com.phonepe.columbus.registry;

/**
 * A registry that supports registering values at runtime.
 *
 * @param <K> the type of the key.
 * @param <V> the type of the values.
 */
public interface MutableRegistry<K, V> extends Registry<K, V> {

    /**
     * Registers the value against the given key.
     *
     * @param key   the key for registration.
     * @param value the value being registered.
     */
    void register(K key, V value);
}
