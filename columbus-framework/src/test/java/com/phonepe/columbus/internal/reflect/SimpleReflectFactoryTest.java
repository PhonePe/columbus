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

import com.phonepe.columbus.reflect.InstanceFactory;
import com.phonepe.columbus.reflect.ReflectFactory;
import com.phonepe.columbus.reflect.ReflectSettings;
import com.phonepe.columbus.reflect.ReflectionInitializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class SimpleReflectFactoryTest {

    private static final ReflectFactory factory = new SimpleReflectFactory();
    private static final Reflections mockReflections = mock();
    private static final ReflectionInitializer mockReflectionInitializer = mock();
    private static final InstanceFactory mockInstanceFactory = mock();

    @Test
    @DisplayName("create with default settings")
    void testCreateDefaults() {
        final var settings = ReflectSettings.defaults();

        final var reflect = factory.create(settings);

        assertNotNull(reflect, "Created instance should not be null");
        assertAll(
                "reflect fields",
                () -> assertTrue(
                        reflect.getMixins().isEmpty(),
                        "No mixins are registered by default"),
                () -> assertInstanceOf(
                        ConstructorInstanceFactory.class, reflect.getInstanceFactory(),
                        "Default instance factory uses constructor"),
                () -> assertNotNull(
                        reflect.getReflections(),
                        "Reflections should be initialized")
        );
    }

    @Test
    @DisplayName("create with custom settings")
    void testCreateWithCustomSettings() {
        final var settings = ReflectSettings.builder()
                .initializerOverride(mockReflectionInitializer)
                .instanceFactory(mockInstanceFactory)
                .mixin(Object.class, String.class)
                .build();

        doReturn(mockReflections).when(mockReflectionInitializer).initialize(settings);

        final var reflect = factory.create(settings);

        assertNotNull(reflect, "Created instance should not be null");
        assertAll(
                "reflect fields",
                () -> assertEquals(
                        1,
                        reflect.getMixins().size(),
                        "Provided mixins are registered"),
                () -> assertSame(
                        mockInstanceFactory, reflect.getInstanceFactory(),
                        "Instance factory is used from settings"),
                () -> assertSame(
                        mockReflections,
                        reflect.getReflections(),
                        "Reflections is initialized from override")
        );
    }
}
