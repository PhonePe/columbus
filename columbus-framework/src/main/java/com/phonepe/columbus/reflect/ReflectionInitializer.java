package com.phonepe.columbus.reflect;

import org.reflections.Reflections;

/**
 * A factory to initialize {@link Reflections} from {@link ReflectSettings}.
 */
public interface ReflectionInitializer {

    Reflections initialize(ReflectSettings settings);
}
