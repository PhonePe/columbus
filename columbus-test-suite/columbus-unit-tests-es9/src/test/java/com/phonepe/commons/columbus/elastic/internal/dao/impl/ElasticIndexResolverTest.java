package com.phonepe.commons.columbus.elastic.internal.dao.impl;

import com.phonepe.commons.columbus.core.toolkit.Toolkit;
import com.phonepe.commons.columbus.core.utils.RegistryCreators;
import com.phonepe.commons.columbus.dao.index.Index;
import com.phonepe.commons.columbus.dao.index.IndexResolver;
import com.phonepe.commons.columbus.dao.index.Indices;
import com.phonepe.commons.columbus.elastic9.dao.index.ElasticIndex;
import com.phonepe.commons.columbus.elastic9.dao.index.IndexToElastic;
import com.phonepe.commons.columbus.elastic9.internal.dao.impl.ElasticIndexResolver;
import com.phonepe.commons.columbus.reflect.Generics;
import com.phonepe.commons.columbus.registry.Registries;
import com.phonepe.commons.columbus.test.annotations.WithToolkit;
import com.phonepe.commons.columbus.visitor.VisitorHelpers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WithToolkit.All
class ElasticIndexResolverTest {

    private static final String KEY = "key";
    private static final String INDEX = "index.1";
    private static IndexResolver<ElasticIndex, Object> indexResolver;

    @Test
    @DisplayName("index name")
    void testIndexName() {
        final var index = Indices.name(INDEX);

        final var elasticIndex = indexResolver.resolve(index);

        assertAll(
                "index name",
                () -> assertNotNull(elasticIndex, "must return a valid index"),
                () -> assertEquals(INDEX, elasticIndex.getName(), "must match given name"));
    }

    @Test
    @DisplayName("index reference")
    void testIndexReference() {
        final var index = Indices.reference(KEY);

        final var elasticIndex = indexResolver.resolve(index, null);

        assertAll(
                "index reference",
                () -> assertNotNull(elasticIndex, "must return a valid index"),
                () -> assertEquals(INDEX, elasticIndex.getName(), "must match configured name"));
    }

    @Test
    @DisplayName("missing index reference")
    void testMissingIndexReference() {
        final var index = Indices.reference("missing");

        assertThrows(IllegalArgumentException.class, () -> indexResolver.resolve(index),
                "must not resolve to a valid index");
    }

    @BeforeAll
    static void beforeAll(final Toolkit toolkit) {
        indexResolver = ElasticIndexResolver.builder()
                .resolver(VisitorHelpers.resolver(RegistryCreators.createVisitorRegistry(
                        toolkit.visitorFactory(),
                        Generics.<IndexToElastic<Index, ElasticIndexResolver>>typed(IndexToElastic.class),
                        ElasticIndexResolver.class,
                        Index.class,
                        Index::getType,
                        null)))
                .indexRegistry(Registries.withValues(Map.of(KEY, INDEX)))
                .build();
    }
}
