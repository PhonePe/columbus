package com.phonepe.columbus.registry;

import com.phonepe.columbus.internal.registry.CachedRegistry;
import com.phonepe.columbus.internal.registry.ChainedRegistry;
import com.phonepe.columbus.internal.registry.EmptyRegistry;
import com.phonepe.columbus.internal.registry.HierarchicalKeyRegistry;
import com.phonepe.columbus.internal.registry.KeyTransformedRegistry;
import com.phonepe.columbus.internal.registry.MonitoredRegistry;
import com.phonepe.columbus.internal.registry.MutableCachedRegistry;
import com.phonepe.columbus.internal.registry.StoredRegistry;
import com.phonepe.columbus.internal.registry.TransformedRegistry;
import com.phonepe.columbus.internal.registry.ValueTransformedRegistry;
import com.phonepe.columbus.reflect.Generics;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * A factory class to create different registries.
 */
@UtilityClass
public class Registries {

    public <K, V> Registry<K, V> empty() {
        return EmptyRegistry.create();
    }

    public <K, V> Registry<K, V> withValues(final Map<K, V> entries) {
        if (entries.isEmpty()) {
            return empty();
        }
        return stored(entries);
    }

    public <K, V> Registry<K, V> cached(final Registry<? super K, ? extends V> registry) {
        if (isEmpty(registry)) {
            return empty();
        }
        return CachedRegistry.<K, V>builder()
                .delegate(registry)
                .build();
    }

    public <K, T, R, V> Registry<K, V> transforming(
            final Function<? super K, ? extends T> keyTransformer,
            final Registry<? super T, ? extends R> registry,
            final Function<? super R, ? extends V> valueTransformer
    ) {
        if (isEmpty(registry)) {
            return empty();
        }
        return TransformedRegistry.<K, T, R, V>builder()
                .keyTransformer(keyTransformer)
                .registry(registry)
                .valueTransformer(valueTransformer)
                .build();
    }

    public <K, T, V> Registry<K, V> transformingKey(
            final Function<? super K, ? extends T> keyTransformer,
            final Registry<? super T, ? extends V> registry
    ) {
        if (isEmpty(registry)) {
            return empty();
        }
        return KeyTransformedRegistry.<K, T, V>builder()
                .keyTransformer(keyTransformer)
                .registry(registry)
                .build();
    }

    public <K, T, V> Registry<K, V> transformingValue(
            final Registry<? super K, ? extends T> registry,
            final Function<? super T, ? extends V> valueTransformer
    ) {
        if (isEmpty(registry)) {
            return empty();
        }
        return ValueTransformedRegistry.<K, T, V>builder()
                .valueTransformer(valueTransformer)
                .registry(registry)
                .build();
    }

    public <K, V> Registry<K, V> monitored(
            final Registry<? super K, ? extends V> registry,
            final MonitoredRegistry.Listener<K, V> listener
    ) {
        return MonitoredRegistry.<K, V>builder()
                .delegate(registry)
                .listener(listener)
                .build();
    }

    public <K, V> Registry<K, V> monitorIfNotEmpty(
            final Registry<? super K, ? extends V> registry,
            final MonitoredRegistry.Listener<K, V> listener
    ) {
        if (isEmpty(registry)) {
            return empty();
        }
        return monitored(registry, listener);
    }

    @SafeVarargs
    public <K, V> RegistryChainBuilder<K, V> chaining(
            final Registry<? super K, ? extends V>... registries
    ) {
        final RegistryChainBuilder<K, V> chain = ChainedRegistry.builder();
        Arrays.stream(registries).forEachOrdered(chain::add);
        return chain;
    }

    public <K, V> MutableRegistry<K, V> stored() {
        return stored(new HashMap<>());
    }

    public <K, V> MutableRegistry<K, V> stored(final Map<K, V> entries) {
        return StoredRegistry.<K, V>builder()
                .entries(entries)
                .build();
    }

    public <K, V> MutableRegistry<K, V> mutableCached(final MutableRegistry<K, V> registry) {
        return MutableCachedRegistry.<K, V>builder()
                .delegate(registry)
                .build();
    }

    public <K, V> Registry<K, V> hierarchicalKey(
            final Registry<? super K, ? extends V> registry,
            final Function<K, Optional<? extends K>> nextKeyFunction
    ) {
        if (isEmpty(registry)) {
            return empty();
        }
        return HierarchicalKeyRegistry.<K, V>builder()
                .nextKeyFunction(nextKeyFunction)
                .registry(registry)
                .build();
    }

    public <K, V> boolean isEmpty(final Registry<K, V> registry) {
        return registry instanceof EmptyRegistry;
    }

    public <K, V> Registry<K, V> usingClassHierarchy(
            final Registry<? super Class<?>, ? extends V> registry,
            final Class<K> baseType
    ) {
        return transformingKey(
                Object::getClass,
                Registries.<Class<?>, V>hierarchicalKey(
                        registry,
                        type -> Optional.ofNullable(Generics.getSuperTypeTowards(type, baseType))));
    }
}
