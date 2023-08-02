package com.phonepe.platform.columbus.type;

import com.phonepe.platform.columbus.registry.Registry;

/**
 * A registry to store {@link JavaType}s, with a unique name.
 */
public interface TypeRegistry extends Registry<String, JavaType<?>> {
}
