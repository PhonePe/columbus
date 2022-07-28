package com.phonepe.platform.columbus.core.utils;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.core.constants.ColumbusConstants;
import lombok.experimental.UtilityClass;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.List;

@UtilityClass
public class ReflectionUtils {
    private Reflections reflections;

    public void initialize(Reflections reflections) {
        ReflectionUtils.reflections = reflections;
    }

    public Reflections getReflections() {
        if (reflections == null) {
            initialize(new Reflections(ColumbusConstants.BASE_PACKAGE));
        }
        return reflections;
    }

    @SuppressWarnings("java:S3958") // SonarQube fix
    public <T, A extends Annotation> List<Class<? extends T>> getAnnotatedSubtypes(
        Reflections reflections,
        Class<T> base,
        Class<A> annotation
    ) {
        return reflections.getSubTypesOf(base).stream()
            .filter(subType -> subType.isAnnotationPresent(annotation))
            .toList();
    }

    public <T, A extends Annotation> List<Class<? extends T>> getAnnotatedSubtypes(Class<T> base, Class<A> annotation) {
        return getAnnotatedSubtypes(getReflections(), base, annotation);
    }

    public <T> List<Class<? extends T>> getJsonSubtypes(Reflections reflections, Class<T> base) {
        return getAnnotatedSubtypes(reflections, base, JsonTypeName.class);
    }

    public <T> List<Class<? extends T>> getJsonSubtypes(Class<T> base) {
        return getJsonSubtypes(getReflections(), base);
    }
}
