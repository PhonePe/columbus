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
package com.phonepe.columbus.reflect;

import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

/**
 * A set of parameters to configure Reflect as needed.
 */
@Data
@SuperBuilder
public class ReflectSettings {

    @Singular(ignoreNullCollections = true)
    private final List<@NonNull String> packageNames;

    @Singular(value = "classType", ignoreNullCollections = true)
    private final List<@NonNull Class<?>> classes;

    @Singular(ignoreNullCollections = true)
    private final Map<@NonNull Class<?>, @NonNull Class<?>> mixins;

    private final ReflectionInitializer initializerOverride;

    private final InstanceFactory instanceFactory;

    public static ReflectSettings defaults() {
        return builder().build();
    }
}
