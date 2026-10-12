package com.phonepe.columbus.type;

import com.phonepe.columbus.registry.Registry;

/**
 * A registry to store {@link JavaType}s, with a unique name.
 */
public interface TypeRegistry extends Registry<String, JavaType<?>> {
}
