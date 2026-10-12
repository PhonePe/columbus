package com.phonepe.columbus.internal.registry;

import lombok.experimental.UtilityClass;

import javax.annotation.Nullable;
import java.util.Optional;

@UtilityClass
class Transforms {

    String keyTransform(final @Nullable String value) {
        return value == null || value.length() > 10 ? null : value.toLowerCase();
    }

    String valueTransform(final @Nullable Object value) {
        if (value == null) {
            return null;
        }
        final var string = value.toString();
        return string.startsWith("-") ? null : string;
    }

    Optional<String> hierarchicalKeyTransform(final @Nullable String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(value.substring(0, value.length() - 1));
    }
}
