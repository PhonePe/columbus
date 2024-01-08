package com.phonepe.commons.columbus.internal.type;

import com.phonepe.commons.columbus.registry.Registry;
import com.phonepe.commons.columbus.type.JavaType;
import com.phonepe.commons.columbus.type.TypeRegistry;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.Optional;

/**
 * An implementation of {@link TypeRegistry} that delegates the task of fetching the type to a {@link Registry}.
 */
@AllArgsConstructor(staticName = "of")
public class DelegatingTypeRegistry implements TypeRegistry {

    @NonNull
    private final Registry<String, JavaType<?>> delegate;

    @Override
    public Optional<? extends JavaType<?>> get(final String key) {
        return delegate.get(key);
    }
}
