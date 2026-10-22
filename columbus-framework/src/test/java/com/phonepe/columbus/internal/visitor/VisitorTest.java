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
import com.phonepe.columbus.internal.visitor.example.visitors.string.ModelAStringVisitor;
import com.phonepe.columbus.internal.visitor.example.visitors.string.ModelBStringVisitor;
import com.phonepe.columbus.internal.visitor.example.visitors.string.StringVisitor;
import com.phonepe.columbus.registry.Registries;
import com.phonepe.columbus.registry.Registry;
import com.phonepe.columbus.visitor.Visitor;
import com.phonepe.columbus.visitor.VisitorHelpers;
import com.phonepe.columbus.visitor.impl.VisitorResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VisitorTest {

    private static final Registry<Model, ModelVisitor<?, String, StringVisitor>> registry = Registries.transformingKey(
            Model::type,
            Registries.stored(new HashMap<>(Map.of(
                    "A", new ModelAStringVisitor<>(),
                    "B", new ModelBStringVisitor<>()))));

    private static final VisitorResolver<String, Model, String, StringVisitor> resolver =
            VisitorHelpers.resolver(registry);

    private static final Visitor<String, Model, String> visitor = StringVisitor.builder()
            .resolver(resolver)
            .defaultProps("DF")
            .prefix("PF")
            .build();

    @Test
    @DisplayName("visit ModelA without props")
    void testVisitModelA() {
        final var model = ModelA.builder()
                .value(10)
                .build();

        final var value = visitor.visit(model);

        assertEquals("AS:A:PF:10:DF", value, "visitor visits model A");
    }

    @Test
    @DisplayName("visit ModelB without props")
    void testVisitModelB() {
        final var model = ModelB.builder()
                .value("hello")
                .build();

        final var name = visitor.visit(model);

        assertEquals("BS:B:PF:hello:DF", name, "visitor visits model B");
    }

    @Test
    @DisplayName("visit ModelA with props")
    void testVisitModelAWithProps() {
        final var model = ModelA.builder()
                .value(10)
                .build();

        final var value = visitor.visit(model, "prop");

        assertEquals("AS:A:PF:10:prop", value, "visitor visits model A");
    }

    @Test
    @DisplayName("visit ModelB with props")
    void testVisitModelBWithProps() {
        final var model = ModelB.builder()
                .value("hello")
                .build();

        final var name = visitor.visit(model, "prop");

        assertEquals("BS:B:PF:hello:prop", name, "visitor visits model B");
    }

    @Test
    @DisplayName("visitor not found")
    void testVisitorMissing() {
        final var model = ModelC.of();

        assertThrows(IllegalArgumentException.class, () -> visitor.visit(model), "no visitor registered");
    }
}
