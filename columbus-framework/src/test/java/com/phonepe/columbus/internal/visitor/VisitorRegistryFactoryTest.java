/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phonepe.columbus.internal.visitor;

import com.phonepe.columbus.internal.visitor.example.models.Model;
import com.phonepe.columbus.internal.visitor.example.models.ModelA;
import com.phonepe.columbus.internal.visitor.example.models.ModelB;
import com.phonepe.columbus.internal.visitor.example.models.ModelC;
import com.phonepe.columbus.internal.visitor.example.visitors.ModelVisitor;
import com.phonepe.columbus.internal.visitor.example.visitors.ModelVisitorContext;
import com.phonepe.columbus.internal.visitor.example.visitors.number.FloatVisitor;
import com.phonepe.columbus.internal.visitor.example.visitors.number.IntegerVisitor;
import com.phonepe.columbus.internal.visitor.example.visitors.number.NumberVisitor;
import com.phonepe.columbus.internal.visitor.example.visitors.string.ConcreteStringVisitorContext;
import com.phonepe.columbus.internal.visitor.example.visitors.string.StringVisitor;
import com.phonepe.columbus.reflect.Generics;
import com.phonepe.columbus.reflect.Reflect;
import com.phonepe.columbus.reflect.ReflectHelpers;
import com.phonepe.columbus.registry.Registries;
import com.phonepe.columbus.registry.RegistryHelpers;
import com.phonepe.columbus.registry.factory.RegistryFactory;
import com.phonepe.columbus.visitor.VisitorHelpers;
import com.phonepe.columbus.visitor.factory.VisitorRegistryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VisitorRegistryFactoryTest {

    private static final ModelA modelA = ModelA.builder()
            .value(5)
            .build();
    private static final ModelB modelB = ModelB.builder()
            .value("V")
            .build();
    private static final ModelC modelC = ModelC.of();

    private static final Reflect reflect = ReflectHelpers.defaultInstance();
    private static final RegistryFactory registryFactory = RegistryHelpers.factory(reflect);
    private static final VisitorRegistryFactory factory = VisitorHelpers.factory(registryFactory);

    private static <P, C extends ModelVisitorContext<P, C>> void assertVisitorResult(
            final C visitor,
            final Model model,
            final P prop,
            final String withoutProp,
            final String withProp
    ) {
        assertAll(
                "visit %s".formatted(model.type()),
                () -> assertEquals(withoutProp, visitor.visit(model), "must use default prop"),
                () -> assertEquals(withProp, visitor.visit(model, prop), "must use provided prop"));
    }

    private static <P, C extends ModelVisitorContext<P, C>> void assertVisitorMissing(
            final C visitor,
            final Model model,
            final P prop
    ) {
        assertAll(
                "visit %s".formatted(model.type()),
                () -> assertThrows(IllegalArgumentException.class, () -> visitor.visit(model), "no visitor"),
                () -> assertThrows(IllegalArgumentException.class, () -> visitor.visit(model, prop), "no visitor"));
    }

    @Nested
    class StringVisitorTest {

        @Test
        @DisplayName("create visitor registry by name")
        void testCreateVisitorRegistryByName() {
            final var options = VisitorHelpers.optionsForName(
                            Generics.<ModelVisitor<Model, String, StringVisitor>>typed(ModelVisitor.class),
                            StringVisitor.class)
                    .build();
            final var nameRegistry = factory.create(options);
            final var registry = Registries.transformingKey(Model::type, nameRegistry);
            final var resolver = VisitorHelpers.resolver(registry);
            final var visitor = StringVisitor.builder()
                    .resolver(resolver)
                    .prefix("P")
                    .defaultProps("D")
                    .build();

            assertAll(
                    "visitors",
                    () -> assertVisitorMissing(visitor, modelA, "X"),
                    () -> assertVisitorResult(visitor, modelB, "X", "BS:B:P:V:D", "BS:B:P:V:X"),
                    () -> assertVisitorMissing(visitor, modelC, "M"));
        }

        @Test
        @DisplayName("create visitor registry by type")
        void testCreateVisitorRegistryByType() {
            final var options = VisitorHelpers.optionsForType(
                            Generics.<ModelVisitor<Model, String, StringVisitor>>typed(ModelVisitor.class),
                            StringVisitor.class)
                    .build();
            final var typeRegistry = factory.create(options);
            final var registry = Registries.usingClassHierarchy(typeRegistry, Model.class);
            final var resolver = VisitorHelpers.resolver(registry);
            final var visitor = StringVisitor.builder()
                    .resolver(resolver)
                    .prefix("P")
                    .defaultProps("D")
                    .build();

            assertAll(
                    "visitors",
                    () -> assertVisitorResult(visitor, modelA, "X", "AS:A:P:5:D", "AS:A:P:5:X"),
                    () -> assertVisitorMissing(visitor, modelB, "X"),
                    () -> assertVisitorMissing(visitor, modelC, "M"));
        }
    }

    @Nested
    class ConcreteStringVisitorTest {

        @Test
        @DisplayName("create visitor registry")
        void testCreateVisitorRegistry() {
            final var options = VisitorHelpers.optionsForName(
                            Generics.<ModelVisitor<Model, String, ConcreteStringVisitorContext>>typed(ModelVisitor.class),
                            ConcreteStringVisitorContext.class)
                    .build();
            final var nameRegistry = factory.create(options);
            final var registry = Registries.transformingKey(Model::type, nameRegistry);
            final var resolver = VisitorHelpers.resolver(registry);
            final var visitor = ConcreteStringVisitorContext.builder()
                    .resolver(resolver)
                    .prefix("P")
                    .defaultProps("D")
                    .build();

            assertAll(
                    "visitors",
                    () -> assertVisitorMissing(visitor, modelA, "X"),
                    () -> assertVisitorMissing(visitor, modelB, "X"),
                    () -> assertVisitorResult(visitor, modelC, "M", "CS:C:P:D", "CS:C:P:M"));
        }
    }

    @Nested
    class NumberVisitorTest {

        @Test
        @DisplayName("create visitor registry")
        void testCreateVisitorRegistry() {
            final var options = VisitorHelpers.optionsForName(
                            Generics.<ModelVisitor<Model, Number, NumberVisitor>>typed(ModelVisitor.class),
                            NumberVisitor.class)
                    .build();
            final var nameRegistry = factory.create(options);
            final var registry = Registries.transformingKey(Model::type, nameRegistry);
            final var resolver = VisitorHelpers.resolver(registry);
            final var visitor = NumberVisitor.builder()
                    .resolver(resolver)
                    .prefix("P")
                    .defaultProps(3)
                    .build();

            assertAll(
                    "visitors",
                    () -> assertVisitorResult(visitor, modelA, 7, "AN:A:P:5:3", "AN:A:P:5:7"),
                    () -> assertVisitorResult(visitor, modelB, 7, "BN:B:P:V:3", "BN:B:P:V:7"),
                    () -> assertVisitorMissing(visitor, modelC, 7));
        }
    }

    @Nested
    class IntegerVisitorTest {

        @Test
        @DisplayName("create visitor registry")
        void testCreateVisitorRegistry() {
            final var options = VisitorHelpers.optionsForName(
                            Generics.<ModelVisitor<Model, Integer, IntegerVisitor>>typed(ModelVisitor.class),
                            IntegerVisitor.class)
                    .build();
            final var nameRegistry = factory.create(options);
            final var registry = Registries.transformingKey(Model::type, nameRegistry);
            final var resolver = VisitorHelpers.resolver(registry);
            final var visitor = IntegerVisitor.builder()
                    .resolver(resolver)
                    .prefix("P")
                    .defaultProps(3)
                    .build();

            assertAll(
                    "visitors",
                    () -> assertVisitorResult(visitor, modelA, 7, "AI:A:P:5:3", "AI:A:P:5:7"),
                    () -> assertVisitorResult(visitor, modelB, 7, "BA:B:P:V:3", "BA:B:P:V:7"),
                    () -> assertVisitorResult(visitor, modelC, 7, "CN:C:P:3", "CN:C:P:7"));
        }
    }

    @Nested
    class FloatVisitorTest {

        @Test
        @DisplayName("create visitor registry")
        void testCreateVisitorRegistry() {
            final var options = VisitorHelpers.optionsForName(
                            Generics.<ModelVisitor<Model, Float, FloatVisitor>>typed(ModelVisitor.class),
                            FloatVisitor.class)
                    .build();
            final var nameRegistry = factory.create(options);
            final var registry = Registries.transformingKey(Model::type, nameRegistry);
            final var resolver = VisitorHelpers.resolver(registry);
            final var visitor = FloatVisitor.builder()
                    .resolver(resolver)
                    .prefix("P")
                    .defaultProps(3.1f)
                    .build();

            assertAll(
                    "visitors",
                    () -> assertVisitorResult(visitor, modelA, 7.1f, "AF:A:P:5:3.1", "AF:A:P:5:7.1"),
                    () -> assertVisitorResult(visitor, modelB, 7.1f, "BA:B:P:V:3.1", "BA:B:P:V:7.1"),
                    () -> assertVisitorResult(visitor, modelC, 7.1f, "CN:C:P:3.1", "CN:C:P:7.1"));
        }
    }
}
