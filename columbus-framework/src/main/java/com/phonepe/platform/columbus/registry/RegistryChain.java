package com.phonepe.platform.columbus.registry;

import lombok.NonNull;

import java.util.List;

/**
 * A registry that uses a collection of registries to fetch the registered value. The registries are expected to have
 * a certain order / hierarchy and the first non-empty value returned is used.
 *
 * @param <K> the type of the key.
 * @param <V> the type of the values.
 */
public interface RegistryChain<K, V> extends Registry<K, V> {

    /**
     * Getter for internal registries.
     *
     * @return list of registries.
     */
    @SuppressWarnings("java:S1452")
    List<@NonNull Registry<? super K, ? extends V>> registries();
}
