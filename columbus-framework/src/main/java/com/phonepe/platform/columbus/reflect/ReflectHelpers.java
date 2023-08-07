package com.phonepe.platform.columbus.reflect;

import com.phonepe.platform.columbus.internal.reflect.ConstructorInstanceFactory;
import com.phonepe.platform.columbus.internal.reflect.SimpleReflectFactory;
import com.phonepe.platform.columbus.internal.reflect.SimpleReflectionInitializer;
import lombok.experimental.UtilityClass;

/**
 * A collection of helper methods to create different types of instances required to set up {@link Reflect}.
 */
@UtilityClass
public class ReflectHelpers {

    public ConstructorInstanceFactory instanceFactory() {
        return new ConstructorInstanceFactory();
    }

    public SimpleReflectionInitializer reflectionInitializer() {
        return new SimpleReflectionInitializer();
    }

    public SimpleReflectFactory reflectFactory() {
        return new SimpleReflectFactory();
    }

    public Reflect defaultInstance() {
        return reflectFactory().create(ReflectSettings.defaults());
    }
}
