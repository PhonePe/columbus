package com.phonepe.columbus.registry;

/**
 * An object that helps to build a chain of registries.
 * <p>
 * The builder can choose to retain the structure or flatten the inner registries.
 *
 * @param <K> the type of the key.
 * @param <V> the type of the values.
 */
public interface RegistryChainBuilder<K, V> {

    RegistryChainBuilder<K, V> add(Registry<? super K, ? extends V> registry);

    RegistryChain<K, V> build();
}
