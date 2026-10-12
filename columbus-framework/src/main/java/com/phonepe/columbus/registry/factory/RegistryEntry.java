package com.phonepe.columbus.registry.factory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

/**
 * A holder for the annotation and the annotated class. This class is used by {@link RegistryFactory} to allow
 * extraction of the key for the registry. {@link RegistryOptions} allows customizing how the key is extracted.
 *
 * @param <T> the type of the annotated class.
 */
@Data
@Accessors(fluent = true)
@AllArgsConstructor(staticName = "of")
public final class RegistryEntry<T> {

    @NonNull
    private final RegistryKey key;

    @NonNull
    private final Class<T> value;

    public String name() {
        return this.key.name();
    }

    public Class<?> type() {
        return this.key.type();
    }

    public int priority() {
        return this.key.priority();
    }
}
