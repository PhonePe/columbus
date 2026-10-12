package com.phonepe.columbus.internal.reflect;

import com.phonepe.columbus.common.Constants;
import com.phonepe.columbus.reflect.ReflectSettings;
import com.phonepe.columbus.reflect.ReflectionInitializer;
import org.reflections.Reflections;

import java.util.List;

/**
 * Creates {@link org.reflections.Reflections Reflections} with the packages and classes provided by
 * {@link ReflectSettings}, along with the base package(s) of Columbus.
 */
public class SimpleReflectionInitializer implements ReflectionInitializer {

    @Override
    public Reflections initialize(final ReflectSettings settings) {
        return new Reflections(getDefaultPackages(), settings.getPackageNames(), settings.getClasses());
    }

    protected List<String> getDefaultPackages() {
        return List.of(Constants.BASE_PACKAGE);
    }
}
