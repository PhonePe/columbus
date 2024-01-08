package com.phonepe.commons.columbus.reflect;

import com.phonepe.commons.columbus.common.pair.Pair;
import com.phonepe.commons.columbus.reflect.SampleInterface.SampleWithAnnotation;
import com.phonepe.commons.columbus.reflect.SampleInterface.SampleWithMixin;
import com.phonepe.commons.columbus.reflect.SampleInterface.SampleWithMixinOverride;
import com.phonepe.commons.columbus.reflect.SampleInterface.SampleWithoutAnnotation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReflectTest {

    private static final Reflect reflect = ReflectHelpers.reflectFactory()
            .create(ReflectSettings.builder()
                    .mixin(SampleWithMixin.class, SampleMixin.class)
                    .mixin(SampleWithMixinOverride.class, SampleMixin.class)
                    .initializerOverride(ReflectHelpers.reflectionInitializer())
                    .instanceFactory(ReflectHelpers.instanceFactory())
                    .build());

    @Nested
    @DisplayName("annotated type")
    class MixinTest {

        @Test
        @DisplayName("returns registered mixin")
        void testGetAnnotatedType() {
            assertEquals(SampleMixin.class, reflect.getAnnotatedType(SampleWithMixin.class));
        }

        @Test
        @DisplayName("returns input if no mixin is registered")
        void testGetAnnotatedTypeReturnsSelf() {
            assertEquals(SampleWithAnnotation.class, reflect.getAnnotatedType(SampleWithAnnotation.class));
        }
    }

    @Nested
    @DisplayName("clone without mixins")
    class MixinCloneTest {

        @Test
        @DisplayName("creates a clone without mixins")
        void testCloneWithoutMixin() {
            final var withoutMixin = reflect.withoutMixins();

            assertTrue(withoutMixin.getMixins().isEmpty(), "no mixins are passed on");
        }

        @Test
        @DisplayName("does not create multiple clones without mixins")
        void testCloneWithoutMixinMultiple() {
            final var withoutMixin = reflect.withoutMixins();
            final var withoutMixinClone = withoutMixin.withoutMixins();

            assertTrue(withoutMixin.getMixins().isEmpty(), "no mixins are passed on");
            assertSame(withoutMixin, withoutMixinClone, "clone is not created if there are no mixins");
        }
    }

    @Nested
    @DisplayName("get annotation")
    class AnnotationTest {

        @Test
        @DisplayName("gets an annotation from the type")
        void testGetAnnotation() {
            final var annotation = reflect.getAnnotation(SampleWithAnnotation.class, SampleAnnotation.class);

            assertAll(
                    "annotation from type",
                    () -> assertNotNull(annotation, "existing annotation is returned"),
                    () -> assertEquals("sample", annotation.name(), "annotation has same name"));
        }

        @Test
        @DisplayName("no matching annotation")
        void testGetNoAnnotation() {
            final var annotation = reflect.getAnnotation(SampleWithoutAnnotation.class, SampleAnnotation.class);

            assertNull(annotation, "no matching annotation should be found");
        }

        @Test
        @DisplayName("gets the annotation from the registered mixin")
        void testGetAnnotationFromMixin() {
            final var annotation = reflect.getAnnotation(SampleWithMixin.class, SampleAnnotation.class);

            assertAll(
                    "annotation from mixin",
                    () -> assertNotNull(annotation, "mixin annotation is returned"),
                    () -> assertEquals("mixin", annotation.name(), "annotation has the name in mixin"));
        }

        @Test
        @DisplayName("annotation on mixin takes priority")
        void testGetAnnotationFromMixinOverride() {
            final var annotation = reflect.getAnnotation(SampleWithMixinOverride.class, SampleAnnotation.class);
            final var actualAnnotation = reflect.withoutMixins()
                    .getAnnotation(SampleWithMixinOverride.class, SampleAnnotation.class);

            assertAll(
                    "annotation from mixin",
                    () -> assertNotNull(annotation, "mixin annotation is returned"),
                    () -> assertEquals("mixin", annotation.name(), "annotation has the name in mixin"));
            assertAll(
                    "annotation from type",
                    () -> assertNotNull(actualAnnotation, "class annotation is returned"),
                    () -> assertEquals("class", actualAnnotation.name(), "annotation has the name in class"));
        }
    }

    @Nested
    @DisplayName("get all annotations")
    class AnnotationsTest {

        @Test
        @DisplayName("gets all annotations from the type")
        void testGetAnnotations() {
            final var annotations = reflect.getAnnotations(SampleWithAnnotation.class, SampleAnnotation.class);

            assertAll(
                    "annotations from type",
                    () -> assertEquals(1, annotations.size(), "class annotation is returned"),
                    () -> assertEquals("sample", annotations.get(0).name(), "annotation has the name in class"));
        }

        @Test
        @DisplayName("no matching annotations")
        void testGetNoAnnotations() {
            final var annotations = reflect.getAnnotations(SampleWithoutAnnotation.class, SampleAnnotation.class);

            assertTrue(annotations.isEmpty(), "no matching annotations should be found");
        }

        @Test
        @DisplayName("gets the annotations from the registered mixin")
        void testGetAnnotationsFromMixin() {
            final var annotations = reflect.getAnnotations(SampleWithMixin.class, SampleAnnotation.class);

            assertAll(
                    "annotations from mixin",
                    () -> assertEquals(1, annotations.size(), "mixin annotation is returned"),
                    () -> assertEquals("mixin", annotations.get(0).name(), "annotation has the name in mixin"));
        }

        @Test
        @DisplayName("annotations on mixin take priority")
        void testGetAnnotationsFromMixinOverride() {
            final var annotations = reflect.getAnnotations(SampleWithMixinOverride.class, SampleAnnotation.class);
            final var actualAnnotations = reflect.withoutMixins()
                    .getAnnotations(SampleWithMixinOverride.class, SampleAnnotation.class);

            assertAll(
                    "annotations from mixin",
                    () -> assertEquals(1, annotations.size(), "mixin annotation is returned"),
                    () -> assertEquals("mixin", annotations.get(0).name(), "annotation has the name in mixin"));
            assertAll(
                    "annotation from type",
                    () -> assertEquals(1, actualAnnotations.size(), "class annotation is returned"),
                    () -> assertEquals("class", actualAnnotations.get(0).name(), "annotation has the name in class"));
        }
    }

    @Nested
    @DisplayName("get subtypes and their annotations")
    class SubTypeAnnotationsTest {

        @Test
        @DisplayName("get sub types of given type")
        void testGetSubTypes() {
            final var expectedTypes = Set.of(
                    SampleWithAnnotation.class,
                    SampleWithoutAnnotation.class,
                    SampleWithMixin.class,
                    SampleWithMixinOverride.class
            );
            final var subTypes = reflect.getSubTypes(SampleInterface.class);

            assertEquals(expectedTypes, subTypes, "match all subtype entries");
        }

        @Test
        @DisplayName("get sub types of given type with given annotation")
        void testGetAnnotatedSubTypes() {
            final var expectedTypes = Set.of(
                    SampleWithAnnotation.class,
                    SampleWithMixin.class,
                    SampleWithMixinOverride.class
            );
            final var subTypes = reflect.getAnnotatedSubTypes(SampleInterface.class, SampleAnnotation.class);

            assertAll(
                    "subtypes",
                    () -> assertEquals(3, subTypes.size(), "match count of subtypes"),
                    () -> assertEquals(
                            expectedTypes,
                            subTypes.stream().map(Pair::first).collect(Collectors.toSet()),
                            "match all subtype entries"),
                    () -> assertAll(
                            "subtype annotations",
                            subTypes.stream()
                                    .map(Pair::second)
                                    .map(annotations -> () ->
                                            assertEquals(1, annotations.size(), "only one annotation")
                                    )));
        }

        @Test
        @DisplayName("get types with the given annotation")
        void testGetAnnotatedTypes() {
            final var expectedTypes = Set.of(
                    SampleMixin.class,
                    SampleWithAnnotation.class,
                    SampleWithMixin.class,
                    SampleWithMixinOverride.class
            );
            final var types = reflect.getAnnotatedTypes(SampleAnnotation.class);

            assertEquals(expectedTypes, types, "match all annotated types");
        }
    }
}
