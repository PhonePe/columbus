package com.phonepe.platform.columbus.internal.visitor;

import com.phonepe.platform.columbus.internal.visitor.Model.TypeA;
import com.phonepe.platform.columbus.internal.visitor.Model.TypeB;
import com.phonepe.platform.columbus.internal.visitor.Model.TypeC;
import com.phonepe.platform.columbus.internal.visitor.Visitors.NameVisitor;
import com.phonepe.platform.columbus.internal.visitor.Visitors.NameVisitorContext;
import com.phonepe.platform.columbus.internal.visitor.Visitors.TypeANameVisitor;
import com.phonepe.platform.columbus.internal.visitor.Visitors.TypeBNameVisitor;
import com.phonepe.platform.columbus.registry.Registries;
import com.phonepe.platform.columbus.registry.Registry;
import com.phonepe.platform.columbus.visitor.Visitor;
import com.phonepe.platform.columbus.visitor.VisitorHelpers;
import com.phonepe.platform.columbus.visitor.impl.VisitorResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VisitorTest {

    private static final Registry<Model, NameVisitor<?>> registry = Registries.transformingKey(
            Model::type,
            Registries.stored(new HashMap<>(Map.of(
                    "A", new TypeANameVisitor(),
                    "B", new TypeBNameVisitor()))));

    private static final VisitorResolver<String, Model, NameVisitorContext> resolver =
            VisitorHelpers.resolver(registry);

    private static final Visitor<String, Model> visitor = NameVisitorContext.builder()
            .resolver(resolver)
            .build();

    @Test
    @DisplayName("visit TypeA")
    void testVisitTypeA() {
        final var model = TypeA.builder()
                .value(10)
                .build();

        final var name = visitor.visit(model);

        assertEquals("A(value=10)", name, "visitor visits type A");
    }

    @Test
    @DisplayName("visit TypeB")
    void testVisitTypeB() {
        final var model = TypeB.builder()
                .value("hello")
                .build();

        final var name = visitor.visit(model);

        assertEquals("B(value='hello')", name, "visitor visits type B");
    }

    @Test
    @DisplayName("visitor not found")
    void testVisitorMissing() {
        final var model = TypeC.of();

        assertThrows(IllegalArgumentException.class, () -> visitor.visit(model), "no visitor registered");
    }
}
