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
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * A collection of utilities to transform data.
 */
@Slf4j
@UtilityClass
public class Transforms {

    public static <E extends Enum<E>> E toEnumValue(
            final String value,
            final String name,
            final Function<String, E> transformer
    ) {
        try {
            return transformer.apply(value);
        } catch (Exception e) {
            log.error("Invalid value '{}' for enum '{}'", value, name, e);
            throw new IllegalArgumentException("Failed to read enum " + name + " for value " + value, e);
        }
    }

    public static Integer parseInteger(final Object object) {
        return parseInteger(object, null);
    }

    public static Integer parseInteger(final Object object, @Nullable final Integer defaultValue) {
        if (object instanceof Integer integer) {
            return integer;
        }
        if (object instanceof String string) {
            try {
                return Integer.parseInt(string);
            } catch (NumberFormatException ignored) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
}
