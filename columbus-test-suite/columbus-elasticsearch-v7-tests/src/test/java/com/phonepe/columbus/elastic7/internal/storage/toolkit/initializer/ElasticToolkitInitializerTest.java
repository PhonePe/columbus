package com.phonepe.columbus.elastic7.internal.storage.toolkit.initializer;

import com.phonepe.columbus.common.http.Endpoint;
import com.phonepe.columbus.common.initializer.Initializer;
import com.phonepe.columbus.core.toolkit.Toolkit;
import com.phonepe.columbus.elastic7.internal.storage.toolkit.DefaultElasticToolkitInitializer;
import com.phonepe.columbus.elastic7.internal.storage.toolkit.ElasticToolkitData;
import com.phonepe.columbus.elastic7.storage.configuration.ElasticConfiguration;
import com.phonepe.columbus.elastic7.storage.configuration.HostConfiguration;
import com.phonepe.columbus.elastic7.storage.configuration.IndexConfiguration;
import com.phonepe.columbus.storage.settings.StorageSettings;
import com.phonepe.columbus.test.annotations.WithToolkit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WithToolkit.All
class ElasticToolkitInitializerTest {

    private static final ElasticConfiguration CONFIGURATION = ElasticConfiguration.builder()
            .enabled(false)
            .name("elastic-test")
            .hosts(HostConfiguration.builder()
                    .endpoint(Endpoint.builder()
                            .host("dummy")
                            .build())
                    .build())
            .indices(IndexConfiguration.builder()
                    .indexMapping("data_read", "columbus.data.v1.index")
                    .indexMapping("data_write", "columbus.data.v2.index")
                    .build())
            .build();

    @Test
    @DisplayName("validate initialization step by step")
    void testInitializeInSteps(final Toolkit toolkit) {

        final var initializer = new DefaultElasticToolkitInitializer();

        verifyAtStart(initializer);

        initializer.use(toolkit);
        verifyWithToolkit(initializer);

        initializer.use(StorageSettings.defaults());
        verifyWithSettings(initializer);

        initializer.use(CONFIGURATION);
        verifyWithConfiguration(initializer);

        final var elasticToolkit = initializer.get();

        assertAll(
                "elastic toolkit",
                () -> assertInstanceOf(ElasticToolkitData.class, elasticToolkit, "must return a data instance"),
                () -> verifyPresent(elasticToolkit.serDe(), "serDe"),
                () -> verifyPresent(elasticToolkit.idResolver(), "id resolver"),
                () -> verifyPresent(elasticToolkit.indexResolver(), "index resolver"),
                () -> verifyPresent(elasticToolkit.nodeIdResolver(), "node id resolver"),
                () -> verifyPresent(elasticToolkit.queryTransformer(), "query transformer"),
                () -> verifyPresent(elasticToolkit.sortTransformer(), "sort transformer"),
                () -> verifyPresent(elasticToolkit.scoreFunctionTransformer(), "scoreFunction transformer"),
                () -> verifyPresent(elasticToolkit.aggregatorTransformer(), "aggregator transformer"),
                () -> verifyPresent(elasticToolkit.aggregationParserResolver(), "aggregation parser resolver")
        );
    }

    private static void verifyAtStart(final DefaultElasticToolkitInitializer initializer) {
        assertAll(
                "initial",
                () -> assertCannotInitialize(initializer.getIdResolver(), "id resolver"),
                () -> assertCannotInitialize(initializer.getIndexResolver(), "index resolver"),
                () -> assertCannotInitialize(initializer.getNodeIdResolver(), "node id resolver"),
                () -> assertCannotInitialize(initializer.getToolkitData(), "toolkit data"),
                () -> assertCannotInitialize(initializer.getQueryTransformer(), "query transformer"),
                () -> assertCannotInitialize(initializer.getSortTransformer(), "sort transformer"),
                () -> assertCannotInitialize(initializer.getScoreFunctionTransformer(), "score transformer"),
                () -> assertCannotInitialize(initializer.getAggregatorTransformer(), "aggregator transformer"),
                () -> assertCannotInitialize(initializer.getAggregationParserResolver(), "aggregation parser"),
                () -> assertCannotInitialize(initializer.getElasticToolkit(), "toolkit")
        );
    }

    private static void verifyWithToolkit(final DefaultElasticToolkitInitializer initializer) {
        assertAll(
                "with toolkit",
                () -> assertCannotInitialize(initializer.getIdResolver(), "id resolver"),
                () -> assertCannotInitialize(initializer.getIndexResolver(), "index resolver"),
                () -> assertCannotInitialize(initializer.getNodeIdResolver(), "node id resolver"),
                () -> assertCannotInitialize(initializer.getToolkitData(), "toolkit data"),
                () -> assertCannotInitialize(initializer.getQueryTransformer(), "query transformer"),
                () -> assertCannotInitialize(initializer.getSortTransformer(), "sort transformer"),
                () -> assertCannotInitialize(initializer.getScoreFunctionTransformer(), "score transformer"),
                () -> assertCannotInitialize(initializer.getAggregatorTransformer(), "aggregator transformer"),
                () -> assertCannotInitialize(initializer.getAggregationParserResolver(), "aggregation parser"),
                () -> assertCannotInitialize(initializer.getElasticToolkit(), "toolkit")
        );
    }

    private static void verifyWithSettings(final DefaultElasticToolkitInitializer initializer) {
        assertAll(
                "with settings",
                () -> assertCanInitialize(initializer.getIdResolver(), "id resolver"),
                () -> assertCannotInitialize(initializer.getIndexResolver(), "index resolver"),
                () -> assertCanInitialize(initializer.getNodeIdResolver(), "node id resolver"),
                () -> assertCannotInitialize(initializer.getToolkitData(), "toolkit data"),
                () -> assertCannotInitialize(initializer.getQueryTransformer(), "query transformer"),
                () -> assertCannotInitialize(initializer.getSortTransformer(), "sort transformer"),
                () -> assertCannotInitialize(initializer.getScoreFunctionTransformer(), "score transformer"),
                () -> assertCannotInitialize(initializer.getAggregatorTransformer(), "aggregator transformer"),
                () -> assertCannotInitialize(initializer.getAggregationParserResolver(), "aggregation parser"),
                () -> assertCannotInitialize(initializer.getElasticToolkit(), "toolkit")
        );
    }

    private static void verifyWithConfiguration(final DefaultElasticToolkitInitializer initializer) {
        assertAll(
                "with configuration",
                () -> assertCanInitialize(initializer.getIdResolver(), "id resolver"),
                () -> assertCanInitialize(initializer.getIndexResolver(), "index resolver"),
                () -> assertCanInitialize(initializer.getNodeIdResolver(), "node id resolver"),
                () -> assertCanInitialize(initializer.getToolkitData(), "toolkit data"),
                () -> assertCanInitialize(initializer.getQueryTransformer(), "query transformer"),
                () -> assertCanInitialize(initializer.getSortTransformer(), "sort transformer"),
                () -> assertCanInitialize(initializer.getScoreFunctionTransformer(), "score transformer"),
                () -> assertCanInitialize(initializer.getAggregatorTransformer(), "aggregator transformer"),
                () -> assertCanInitialize(initializer.getAggregationParserResolver(), "aggregation parser"),
                () -> assertCanInitialize(initializer.getElasticToolkit(), "toolkit")
        );
    }

    private static <T> void assertCanInitialize(final Initializer<T> init, final String name) {
        assertTrue(init.isPresent(), () -> "%s can be initialized".formatted(name));
    }

    private static <T> void assertCannotInitialize(final Initializer<T> init, final String name) {
        assertFalse(init.isPresent(), () -> "%s cannot be initialized".formatted(name));
    }

    private static <T> void verifyPresent(final T value, final String name) {
        assertNotNull(value, () -> "must have a valid %s".formatted(name));
    }
}
