package com.phonepe.columbus.internal.type;

import com.phonepe.columbus.type.EntityType;
import com.phonepe.columbus.type.JavaType;
import com.phonepe.columbus.type.NamedType;
import com.phonepe.columbus.type.TypeRegistry;
import com.phonepe.columbus.type.TypeResolver;
import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * A resolver that uses a {@link TypeRegistry} to find the registered {@link JavaType} for a given name.
 */
@AllArgsConstructor(staticName = "of")
public class RegisteredTypeResolver implements TypeResolver {

    @NonNull
    private final TypeRegistry typeRegistry;

    @Override
    @SuppressWarnings("unchecked")
    public <T> JavaType<T> toJavaType(final EntityType<T> type) {
        if (type instanceof JavaType<T> javaType) {
            return javaType;
        }
        final var name = ((NamedType<T>) type).getName();
        return (JavaType<T>) typeRegistry.get(name)
                .orElseThrow(() -> new IllegalArgumentException("No type registered for name '%s'".formatted(name)));
    }
}
