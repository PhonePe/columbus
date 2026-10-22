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
