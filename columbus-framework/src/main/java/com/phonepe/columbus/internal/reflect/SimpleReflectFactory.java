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
