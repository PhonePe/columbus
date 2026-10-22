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
package com.phonepe.columbus.internal.reflect;

import com.phonepe.columbus.reflect.Reflect;
import com.phonepe.columbus.reflect.ReflectFactory;
import com.phonepe.columbus.reflect.ReflectSettings;

import java.util.Objects;

/**
 * Creates {@link Reflect} with the {@link org.reflections.Reflections Reflections} provided by
 * {@link ReflectSettings#getInitializerOverride()} or an instance of {@link SimpleReflectionInitializer},
 * and the instance factory provided by {@link ReflectSettings#getInstanceFactory()} or an instance of
 * {@link ConstructorInstanceFactory}.
 */
public class SimpleReflectFactory implements ReflectFactory {

    @Override
    public Reflect create(final ReflectSettings settings) {
        final var initializer = Objects.requireNonNullElseGet(
                settings.getInitializerOverride(),
                SimpleReflectionInitializer::new
        );
        return Reflect.builder()
                .reflections(initializer.initialize(settings))
                .instanceFactory(Objects.requireNonNullElseGet(
                        settings.getInstanceFactory(),
                        ConstructorInstanceFactory::new
                ))
                .mixins(settings.getMixins())
                .build();
    }
}
