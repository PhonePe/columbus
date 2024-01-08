package com.phonepe.commons.columbus.type;

import com.phonepe.commons.columbus.registry.Registry;

/**
 * A registry to store {@link JavaType}s, with a unique name.
 */
public interface TypeRegistry extends Registry<String, JavaType<?>> {
}
