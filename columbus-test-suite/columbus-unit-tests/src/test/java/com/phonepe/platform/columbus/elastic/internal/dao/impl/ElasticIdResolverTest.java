package com.phonepe.platform.columbus.elastic.internal.dao.impl;

import com.phonepe.platform.columbus.core.toolkit.Toolkit;
import com.phonepe.platform.columbus.core.utils.RegistryCreators;
import com.phonepe.platform.columbus.dao.id.Id;
import com.phonepe.platform.columbus.dao.id.IdResolver;
import com.phonepe.platform.columbus.dao.id.Ids;
import com.phonepe.platform.columbus.elastic.dao.id.ElasticId;
import com.phonepe.platform.columbus.elastic.dao.id.IdToElastic;
import com.phonepe.platform.columbus.reflect.Generics;
import com.phonepe.platform.columbus.test.annotations.WithToolkit;
import com.phonepe.platform.columbus.visitor.VisitorHelpers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@WithToolkit.All
class ElasticIdResolverTest {

    static IdResolver<ElasticId> idResolver;

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
