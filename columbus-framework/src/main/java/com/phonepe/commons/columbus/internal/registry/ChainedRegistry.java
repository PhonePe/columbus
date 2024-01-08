package com.phonepe.commons.columbus.internal.registry;

import com.phonepe.commons.columbus.registry.Registry;
import com.phonepe.commons.columbus.registry.RegistryChain;
import com.phonepe.commons.columbus.registry.RegistryChainBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Optional;

/**
 * A registry that uses a list of other registries, in order, to get the value for a given key.
 *
 * @param <K> the type of the key.
 * @param <V> the type of the values.
 */
@Getter
@Builder
public class ChainedRegistry<K, V> implements RegistryChain<K, V> {

    @Accessors(fluent = true)
    @Singular(ignoreNullCollections = true)
    private final List<@NonNull Registry<? super K, ? extends V>> registries;

    @Override
    public Optional<? extends V> get(final K key) {
        return registries.stream()
                .map(registry -> registry.get(key))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    @SuppressWarnings("unused")
    private static class ChainedRegistryBuilder<K, V> implements RegistryChainBuilder<K, V> {

        @Override
        public RegistryChainBuilder<K, V> add(final Registry<? super K, ? extends V> registry) {
            if (registry instanceof RegistryChain<? super K, ? extends V> chain) {
                chain.registries().forEach(this::add);
            } else if (registry != null && !(registry instanceof EmptyRegistry<? super K, ? extends V>)) {
                return registry(registry);
            }
            return this;
        }
    }
}
