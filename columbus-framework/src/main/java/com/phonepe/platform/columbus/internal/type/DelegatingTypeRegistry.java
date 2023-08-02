package com.phonepe.platform.columbus.internal.type;

import com.phonepe.platform.columbus.registry.Registry;
import com.phonepe.platform.columbus.type.JavaType;
import com.phonepe.platform.columbus.type.TypeRegistry;
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
