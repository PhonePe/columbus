package com.phonepe.columbus.type;

import java.lang.reflect.Type;

/**
 * A helper to resolve an {@link EntityType} to a {@link JavaType}.
 */
public interface TypeResolver {

    /**
     * Get the {@link JavaType} representing the given {@link EntityType}.
     *
     * @param type the type to be converted.
     * @param <T>  the type represented by the input.
     * @return {@code type} if it is already a {@link JavaType}, resolve a matching {@link JavaType} otherwise.
     * @throws IllegalArgumentException if the {@code type} is a {@link NamedType} and no {@link JavaType} is
     *                                  registered for that name.
     */
    <T> JavaType<T> toJavaType(EntityType<T> type);

    /**
     * Get the raw {@link Type} representing the given {@link EntityType}.
     * @param type the type to be converted.
     * @return the raw {@link Type} type.
     */
    default Type toRawType(EntityType<?> type) {
        return toJavaType(type).getType().getType();
    }
}
