package com.phonepe.platform.columbus.test.utils;

import com.phonepe.platform.columbus.test.extensions.ValueProps;
import com.phonepe.platform.columbus.test.extensions.ValueProps.Key;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.extension.ExtensionContext.Store;

import java.util.Objects;
import java.util.Optional;

@UtilityClass
public class AnnotatedValueUtils {

    public Optional<Object> getKey(final ValueProps.Key key) {
        if (key != null) {
            if (!Void.class.equals(key.keyClass())) {
                return Optional.of(key.keyClass());
            }
            if (StringUtils.isNotEmpty(key.keyName())) {
                return Optional.of(key.keyName());
            }
        }
        return Optional.empty();
    }

    public <T> T getValue(final Store store, final ValueProps.Key key, final Class<T> type) {
        return store.get(AnnotatedValueUtils.getKey(key).orElse(type), type);
    }

    public static <D> D getRequiredValue(
            final Store store,
            final Object key,
            final Key props,
            final Class<D> type
    ) {
        return Objects.requireNonNull(
                getValue(store, key, props, type),
                () -> "No object found for %s".formatted(type));
    }

    private static <D> D getValue(final Store store, final Object key, final Key props, final Class<D> type) {
        return key != null ? store.get(key, type) : getValue(store, props, type);
    }
}
