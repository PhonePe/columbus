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
package com.phonepe.columbus.test.utils;

import com.phonepe.columbus.test.value.ValueProps;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.extension.ExtensionContext.Store;

import java.util.Objects;
import java.util.Optional;

@UtilityClass
public class AnnotatedValueUtils {

    public static Optional<Object> getKey(final ValueProps.Key key) {
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

    public static <T> T getValue(final Store store, final ValueProps.Key key, final Class<T> type) {
        return store.get(AnnotatedValueUtils.getKey(key).orElse(type), type);
    }

    public static <D> D getRequiredValue(
            final Store store,
            final Object key,
            final ValueProps.Key props,
            final Class<D> type
    ) {
        return Objects.requireNonNull(
                getValue(store, key, props, type),
                () -> "No object found for %s".formatted(type));
    }

    private <D> D getValue(final Store store, final Object key, final ValueProps.Key props, final Class<D> type) {
        return key != null ? store.get(key, type) : getValue(store, props, type);
    }
}
