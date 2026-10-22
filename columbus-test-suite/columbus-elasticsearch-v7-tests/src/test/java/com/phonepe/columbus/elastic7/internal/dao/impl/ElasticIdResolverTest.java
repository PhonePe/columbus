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
package com.phonepe.columbus.elastic7.internal.dao.impl;

import com.phonepe.columbus.core.toolkit.Toolkit;
import com.phonepe.columbus.core.utils.RegistryCreators;
import com.phonepe.columbus.dao.id.Id;
import com.phonepe.columbus.dao.id.IdResolver;
import com.phonepe.columbus.dao.id.Ids;
import com.phonepe.columbus.elastic7.dao.id.ElasticId;
import com.phonepe.columbus.elastic7.dao.id.IdToElastic;
import com.phonepe.columbus.reflect.Generics;
import com.phonepe.columbus.test.annotations.WithToolkit;
import com.phonepe.columbus.visitor.VisitorHelpers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@WithToolkit.All
class ElasticIdResolverTest {

    private static IdResolver<ElasticId, Object> idResolver;

    @Test
    @DisplayName("auto id")
    void testAutoId() {
        final var id = Ids.auto();

        final var elasticId = idResolver.resolve(id);

        assertAll(
                "auto id",
                () -> assertNotNull(elasticId, "must return a valid id"),
                () -> assertNull(elasticId.getId(), "auto id has no value"));
    }

    @Test
    @DisplayName("value id")
    void testValueId() {
        final var value = "id";
        final var id = Ids.value(value);

        final var elasticId = idResolver.resolve(id);

        assertAll(
                "value id",
                () -> assertNotNull(elasticId, "must return a valid id"),
                () -> assertEquals(value, elasticId.getId(), "must match given value"));
    }

    @BeforeAll
    static void beforeAll(final Toolkit toolkit) {
        idResolver = ElasticIdResolver.builder()
                .resolver(VisitorHelpers.resolver(RegistryCreators.createVisitorRegistry(
                        toolkit.visitorFactory(),
                        Generics.<IdToElastic<Id, ElasticIdResolver>>typed(IdToElastic.class),
                        ElasticIdResolver.class,
                        Id.class,
                        Id::getType,
                        null)))
                .build();
    }
}
