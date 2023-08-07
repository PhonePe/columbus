package com.phonepe.platform.columbus.internal.visitor;

import com.phonepe.platform.columbus.internal.visitor.Model.TypeA;
import com.phonepe.platform.columbus.internal.visitor.Model.TypeB;
import com.phonepe.platform.columbus.internal.visitor.Model.TypeC;
import com.phonepe.platform.columbus.internal.visitor.Visitors.BaseNameVisitor;
import com.phonepe.platform.columbus.internal.visitor.Visitors.DummyNameVisitorContext;
import com.phonepe.platform.columbus.internal.visitor.Visitors.NameVisitor;
import com.phonepe.platform.columbus.internal.visitor.Visitors.NameVisitorContext;
import com.phonepe.platform.columbus.reflect.Generics;
import com.phonepe.platform.columbus.reflect.Reflect;
import com.phonepe.platform.columbus.reflect.ReflectHelpers;
import com.phonepe.platform.columbus.registry.Registries;
import com.phonepe.platform.columbus.registry.RegistryHelpers;
import com.phonepe.platform.columbus.registry.factory.RegistryFactory;
import com.phonepe.platform.columbus.visitor.VisitorHelpers;
import com.phonepe.platform.columbus.visitor.factory.VisitorRegistryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VisitorRegistryFactoryTest {

    private static final TypeA modelA = TypeA.builder()
            .value(10)
            .build();
    private static final TypeB modelB = TypeB.builder()
            .value("hello")
            .build();
    private static final TypeC modelC = TypeC.of();

    private static final Reflect reflect = ReflectHelpers.defaultInstance();
    private static final RegistryFactory registryFactory = RegistryHelpers.factory(reflect);
    private static final VisitorRegistryFactory factory = VisitorHelpers.factory(registryFactory);

    @Nested
    class NameVisitorTest {

        @Test
        @DisplayName("create visitor registry")
        void testCreateVisitorRegistry() {
            final var options = VisitorHelpers.optionsForName(
                            Generics.<NameVisitor<?>>typed(NameVisitor.class),
                            NameVisitorContext.class)
                    .build();
            final var nameRegistry = factory.create(options);
            final var registry = Registries.transformingKey(Model::type, nameRegistry);
            final var resolver = VisitorHelpers.resolver(registry);
            final var visitor = NameVisitorContext.builder()
                    .resolver(resolver)
                    .build();

            assertAll(
                    "visitors",
                    () -> assertEquals("A(value=10)", visitor.visit(modelA), "visitor visits type A"),
                    () -> assertEquals("B(value='hello')", visitor.visit(modelB), "visitor visits type B"),
                    () -> assertThrows(
                            IllegalArgumentException.class,
                            () -> visitor.visit(modelC),
                            "no visitor for type C"));
        }

        @Test
        @DisplayName("create visitor registry for base visitor")
        void testCreateVisitorRegistryForBase() {
            final var options = VisitorHelpers.optionsForName(
                            Generics.<BaseNameVisitor<?, NameVisitorContext>>typed(BaseNameVisitor.class),
                            NameVisitorContext.class)
                    .build();
            final var nameRegistry = factory.create(options);
            final var registry = Registries.transformingKey(Model::type, nameRegistry);
            final var resolver = VisitorHelpers.resolver(registry);
            final var visitor = NameVisitorContext.builder()
                    .resolver(resolver)
                    .build();

            assertAll(
                    "visitors",
                    () -> assertEquals("A(value=10)", visitor.visit(modelA), "visitor visits type A"),
                    () -> assertEquals("B(value='hello')", visitor.visit(modelB), "visitor visits type B"),
                    () -> assertEquals("C(NameVisitorContext)", visitor.visit(modelC), "visitor visits type C"));
        }

        @Test
        @DisplayName("create visitor registry for different context")
        void testCreateVisitorRegistryForDifferentContext() {
            final var options = VisitorHelpers.optionsForName(
                            Generics.<BaseNameVisitor<?, DummyNameVisitorContext>>typed(BaseNameVisitor.class),
                            DummyNameVisitorContext.class)
                    .build();
            final var nameRegistry = factory.create(options);
            final var registry = Registries.transformingKey(Model::type, nameRegistry);
            final var resolver = VisitorHelpers.resolver(registry);
            final var visitor = DummyNameVisitorContext.builder()
                    .resolver(resolver)
                    .build();

            assertAll(
                    "visitors",
                    () -> assertThrows(
                            IllegalArgumentException.class,
                            () -> visitor.visit(modelA),
                            "no visitor for type A"),
                    () -> assertThrows(
                            IllegalArgumentException.class,
                            () -> visitor.visit(modelB),
                            "no visitor for type B"),
                    () -> assertEquals("C(DummyNameVisitorContext)", visitor.visit(modelC), "visitor visits type C"));
        }
    }

    @Nested
    class TypeVisitorTest {

        @Test
        @DisplayName("create visitor registry")
        void testCreateVisitorRegistry() {
            final var options = VisitorHelpers.optionsForType(
                            Generics.<NameVisitor<?>>typed(NameVisitor.class),
                            NameVisitorContext.class)
                    .build();
            final var typeRegistry = factory.create(options);
            final var registry = Registries.usingClassHierarchy(typeRegistry, Model.class);
            final var resolver = VisitorHelpers.resolver(registry);
            final var visitor = NameVisitorContext.builder()
                    .resolver(resolver)
                    .build();

            assertAll(
                    "visitors",
                    () -> assertEquals("A(value=10)", visitor.visit(modelA), "visitor visits type A"),
                    () -> assertEquals("B(value='hello')", visitor.visit(modelB), "visitor visits type B"),
                    () -> assertThrows(
                            IllegalArgumentException.class,
                            () -> visitor.visit(modelC),
                            "no visitor for type C"));
        }

        @Test
        @DisplayName("create visitor registry for base visitor")
        void testCreateVisitorRegistryForBase() {
            final var options = VisitorHelpers.optionsForType(
                            Generics.<BaseNameVisitor<?, NameVisitorContext>>typed(BaseNameVisitor.class),
                            NameVisitorContext.class)
                    .build();
            final var typeRegistry = factory.create(options);
            final var registry = Registries.usingClassHierarchy(typeRegistry, Model.class);
            final var resolver = VisitorHelpers.resolver(registry);
            final var visitor = NameVisitorContext.builder()
                    .resolver(resolver)
                    .build();

            assertAll(
                    "visitors",
                    () -> assertEquals("A(value=10)", visitor.visit(modelA), "visitor visits type A"),
                    () -> assertEquals("B(value='hello')", visitor.visit(modelB), "visitor visits type B"),
                    () -> assertEquals("C(NameVisitorContext)", visitor.visit(modelC), "visitor visits type C"));
        }

        @Test
        @DisplayName("create visitor registry for different context")
        void testCreateVisitorRegistryForDifferentContext() {
            final var options = VisitorHelpers.optionsForType(
                            Generics.<BaseNameVisitor<?, DummyNameVisitorContext>>typed(BaseNameVisitor.class),
                            DummyNameVisitorContext.class)
                    .build();
            final var typeRegistry = factory.create(options);
            final var registry = Registries.usingClassHierarchy(typeRegistry, Model.class);
            final var resolver = VisitorHelpers.resolver(registry);
            final var visitor = DummyNameVisitorContext.builder()
                    .resolver(resolver)
                    .build();

            assertAll(
                    "visitors",
                    () -> assertThrows(
                            IllegalArgumentException.class,
                            () -> visitor.visit(modelA),
                            "no visitor for type A"),
                    () -> assertThrows(
                            IllegalArgumentException.class,
                            () -> visitor.visit(modelB),
                            "no visitor for type B"),
                    () -> assertEquals("C(DummyNameVisitorContext)", visitor.visit(modelC), "visitor visits type C"));
        }
    }
}
