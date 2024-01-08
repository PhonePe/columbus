package com.phonepe.commons.columbus.internal.reflect;

import com.phonepe.commons.columbus.reflect.ReflectSettings;
import com.phonepe.commons.columbus.reflect.ReflectionInitializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SimpleReflectionInitializerTest {

    private static final ReflectionInitializer initializer = new SimpleReflectionInitializer();

    @Test
    @DisplayName("initialize reflections")
    void testInitialize() {
        final var settings = ReflectSettings.builder()
                .classType(Map.class)
                .packageName("java.util")
                .build();

        final var reflections = initializer.initialize(settings);

        assertNotNull(reflections, "Initialized reflections should not be null");
    }
}
