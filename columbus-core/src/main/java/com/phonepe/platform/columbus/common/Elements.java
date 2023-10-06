package com.phonepe.platform.columbus.common;

import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * A collection of utilities that operate on the elements in a container.
 */
@UtilityClass
public class Elements {

    public static <T> T getOrDefault(final List<T> list, final int index, final T defaultValue) {
        if (list == null || index < 0 || index >= list.size()) {
            return defaultValue;
        }
        return list.get(index);
    }

    public static <T> T firstOrNull(final List<T> list) {
        return getOrDefault(list, 0, null);
    }
}
