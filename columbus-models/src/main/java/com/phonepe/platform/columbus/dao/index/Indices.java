package com.phonepe.platform.columbus.dao.index;

import lombok.experimental.UtilityClass;

/**
 * A factory class to create different types of {@link Index} instances.
 */
@UtilityClass
public class Indices {

    public IndexName name(final String name) {
        return IndexName.builder()
                .name(name)
                .build();
    }

    public IndexReference reference(final String key) {
        return IndexReference.builder()
                .key(key)
                .build();
    }
}
