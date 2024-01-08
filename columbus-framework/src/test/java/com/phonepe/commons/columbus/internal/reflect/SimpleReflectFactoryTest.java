package com.phonepe.commons.columbus.internal.reflect;

import com.phonepe.commons.columbus.reflect.InstanceFactory;
import com.phonepe.commons.columbus.reflect.ReflectFactory;
import com.phonepe.commons.columbus.reflect.ReflectSettings;
import com.phonepe.commons.columbus.reflect.ReflectionInitializer;
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
