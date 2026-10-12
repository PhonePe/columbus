package com.phonepe.columbus.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.phonepe.columbus.internal.type.DelegatingTypeRegistry;
import com.phonepe.columbus.internal.type.RegisteredTypeResolver;
import com.phonepe.columbus.registry.Registries;
import com.phonepe.columbus.registry.Registry;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Type;

/**
 * A factory class to create different types and their helpers.
 */
@UtilityClass
public class Types {

    public <T> NamedType<T> forName(final String name) {
        return NamedType.of(name);
    }

    public <T> JavaType<T> forClass(final Class<T> type) {
        return forType(type);
    }

    public <T> JavaType<T> forType(final Type type) {
        return forTypeReference(new TypeReference<>() {
            @Override
            public Type getType() {
                return type;
            }
        });
    }

    public <T> JavaType<T> forTypeReference(final TypeReference<T> type) {
        return JavaType.of(type);
    }

    public TypeRegistry typeRegistry(final Registry<String, JavaType<?>> registry) {
        if (registry instanceof TypeRegistry typeRegistry) {
            return typeRegistry;
        }
        return DelegatingTypeRegistry.of(registry);
    }

    public TypeRegistry defaultTypeRegistry() {
        return Types.typeRegistry(Registries.empty());
    }

    public TypeResolver resolver(final TypeRegistry typeRegistry) {
        return RegisteredTypeResolver.of(typeRegistry);
    }
}
