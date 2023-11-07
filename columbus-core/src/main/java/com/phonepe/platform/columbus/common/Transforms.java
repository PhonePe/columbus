package com.phonepe.platform.columbus.common;

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
