/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phonepe.columbus.common;

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

    public static <T> @Nullable T getOrNull(final Map<String, Object> map, final String key, final Class<T> type) {
        return getOrDefault(map, key, null, type);
    }

    public static <T> @Nullable T firstOrNull(final List<T> list) {
        return getOrDefault(list, 0, null);
    }
}
