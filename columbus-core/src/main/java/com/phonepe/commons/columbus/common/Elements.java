package com.phonepe.commons.columbus.common;

import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.MapUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

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

    public static <T> T getOrDefault(final Map<String, Object> map, final String key, final Class<T> type) {
        return getOrDefault(map, key, null, type);
    }

    public static <T> T getOrDefault(
            final Map<String, Object> map,
            final String key,
            @Nullable final T defaultValue,
            final Class<T> type
    ) {
        if (MapUtils.isEmpty(map)) {
            return defaultValue;
        }

        final var value = map.get(key);
        return type.isInstance(value) ? type.cast(value) : defaultValue;
    }

    public static <T> T firstOrNull(final List<T> list) {
        return getOrDefault(list, 0, null);
    }
}
