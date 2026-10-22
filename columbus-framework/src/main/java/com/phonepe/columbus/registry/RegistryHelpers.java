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
package com.phonepe.columbus.registry;

import com.phonepe.columbus.internal.registry.factory.ReflectRegistryFactory;
import com.phonepe.columbus.reflect.Reflect;
import com.phonepe.columbus.registry.factory.RegistryEntry;
import com.phonepe.columbus.registry.factory.RegistryOptions;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Predicate;

/**
 * A collection of helper methods to create different instances required to create a {@link Registry}.
 */
@UtilityClass
public class RegistryHelpers {

    public ReflectRegistryFactory factory(final Reflect reflect) {
        return ReflectRegistryFactory.with(reflect);
    }

    @SuppressWarnings("java:S1452")
    public <V> RegistryOptions.RegistryOptionsBuilder<String, V, ?, ?> optionsForName(final Class<V> baseType) {
        return RegistryOptions.builder(baseType, RegistryEntry::name)
                .keyValidator(StringUtils::isNotEmpty);
    }

    @SuppressWarnings("java:S1452")
    public <V> RegistryOptions.RegistryOptionsBuilder<Class<?>, V, ?, ?> optionsForType(final Class<V> baseType) {
        return RegistryOptions.<Class<?>, V>builder(baseType, RegistryEntry::type)
                .keyValidator(Predicate.not(Void.class::equals));
    }
}
