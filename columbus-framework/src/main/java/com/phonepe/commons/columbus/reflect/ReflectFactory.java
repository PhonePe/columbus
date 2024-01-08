package com.phonepe.commons.columbus.reflect;

/**
 * A factory to initialize {@link Reflect} from {@link ReflectSettings}.
 */
public interface ReflectFactory {

    Reflect create(ReflectSettings settings);
}
