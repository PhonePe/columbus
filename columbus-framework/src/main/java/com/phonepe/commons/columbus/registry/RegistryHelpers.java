package com.phonepe.commons.columbus.registry;

import com.phonepe.commons.columbus.internal.registry.factory.ReflectRegistryFactory;
import com.phonepe.commons.columbus.reflect.Reflect;
import com.phonepe.commons.columbus.registry.factory.RegistryEntry;
import com.phonepe.commons.columbus.registry.factory.RegistryOptions;
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
