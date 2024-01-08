package com.phonepe.commons.columbus.reflect;

import com.phonepe.commons.columbus.common.pair.Pair;
import com.phonepe.commons.columbus.common.pair.PairFunctions;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A set of utilities on reflections.
 */
@Getter
@Builder(toBuilder = true)
public class Reflect {

    @NonNull
    private final Reflections reflections;
    @NonNull
    private final InstanceFactory instanceFactory;
    @Singular(ignoreNullCollections = true)
    private final Map<@NonNull Class<?>, @NonNull Class<?>> mixins;

    /**
     * Finds the class to use for annotations. If a mixin is registered for the given class, the mixin is to be
     * used. Otherwise, the class itself should be used.
     *
     * @param type the class to get mixin for.
     * @return the mixin registered for the {@code type}, or the input {@code type} itself.
     * @implNote Do not register a mixin for a mixin. Only the first registered mixin is fetched, and the path is not
     * traversed.
     */
    public Class<?> getAnnotatedType(final Class<?> type) {
        return mixins.getOrDefault(type, type);
    }

    /**
     * Create a clone of this instance with no mixins registered. Useful to perform operation without mixins for
     * external use cases.
     *
     * @return a new instance with no mixins, if there are mixins registered, otherwise, returns the same instance.
     */
    public Reflect withoutMixins() {
        if (mixins.isEmpty()) {
            return this;
        }
        return toBuilder().clearMixins().build();
    }

    /**
     * Get the annotation of the given type from {@code type} or its mixin.
     *
     * @param type           the type to get annotation from.
     * @param annotationType the type of annotation.
     * @param <T>            represents the type of type.
     * @param <A>            represents the type of the annotation.
     * @return the annotation that is present on {@code type} or its mixin, or null if no annotation is present.
     */
    public <T, A extends Annotation> A getAnnotation(final Class<T> type, final Class<A> annotationType) {
        return getAnnotatedType(type).getAnnotation(annotationType);
    }

    /**
     * Get all the annotations of the given type from {@code type} or its mixin.
     *
     * @param type           the type to get annotation from.
     * @param annotationType the type of annotation.
     * @param <T>            represents the type of type.
     * @param <A>            represents the type of the annotation.
     * @return the annotations that are present on {@code type} or its mixin.
     */
    public <T, A extends Annotation> List<A> getAnnotations(final Class<T> type, final Class<A> annotationType) {
        return List.of(getAnnotatedType(type).getAnnotationsByType(annotationType));
    }

    /**
     * Get all the subtypes of the given type.
     *
     * @param type the type whose subtypes are to be fetched.
     * @param <T>  represents the type of the base type.
     * @return the set of subtypes of the given types.
     */
    public <T> Collection<Class<? extends T>> getSubTypes(final Class<T> type) {
        return reflections.getSubTypesOf(type);
    }

    /**
     * Get all the subtypes of the given type that have the given annotation. The annotations are fetched from the
     * subtype, or its mixin.
     *
     * @param type           the type whose subtypes are to be fetched.
     * @param annotationType the type of annotation.
     * @param <T>            represents the type of the base type.
     * @param <A>            represents the type of the annotation.
     * @return a list of subtypes and their annotations.
     */
    public <T, A extends Annotation> Collection<Pair<Class<? extends T>, List<A>>> getAnnotatedSubTypes(
            final Class<T> type,
            final Class<A> annotationType
    ) {
        return getSubTypes(type).stream()
                .map(subType -> Pair.<Class<? extends T>, List<A>>of(subType, getAnnotations(subType, annotationType)))
                .filter(PairFunctions.filteringSecond(Predicate.not(List::isEmpty)))
                .toList();
    }

    /**
     * Get all the types that have the given annotation. This also includes any types whose mixins have the given
     * annotation.
     *
     * @param annotationClass the type of the annotation.
     * @param <A>             represents the type of the annotation.
     * @return a list of types which have the given annotation.
     */
    public <A extends Annotation> Collection<Class<?>> getAnnotatedTypes(final Class<A> annotationClass) {
        final var annotatedTypes = new HashSet<>(reflections.getTypesAnnotatedWith(annotationClass));
        annotatedTypes.addAll(mixins.entrySet().stream()
                .filter(entry -> annotatedTypes.contains(entry.getValue()))
                .map(Entry::getKey)
                .collect(Collectors.toSet()));
        return Set.copyOf(annotatedTypes);
    }
}
