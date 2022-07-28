package com.phonepe.platform.columbus.core.feature;

@FunctionalInterface
public interface ConfigGetter<T, C> {
    C getConfig(T config);

    @SuppressWarnings("unused")
    static <T, C> C nullConfig(T config) {
        return null;
    }
}
