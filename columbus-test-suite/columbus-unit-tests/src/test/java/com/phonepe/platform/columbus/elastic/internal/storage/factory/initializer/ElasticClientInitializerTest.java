package com.phonepe.platform.columbus.elastic.internal.storage.factory.initializer;

import com.phonepe.platform.columbus.common.http.Endpoint;
import com.phonepe.platform.columbus.core.toolkit.Toolkit;
import com.phonepe.platform.columbus.elastic.storage.configuration.ElasticConfiguration;
import com.phonepe.platform.columbus.elastic.storage.configuration.HostConfiguration;
import com.phonepe.platform.columbus.storage.commands.VersionCommand;
import com.phonepe.platform.columbus.storage.settings.StorageSettings;
import com.phonepe.platform.columbus.test.annotations.WithElasticContainer;
import com.phonepe.platform.columbus.test.annotations.WithMockDropwizardEnvironment;
import com.phonepe.platform.columbus.test.annotations.WithToolkit;
import com.phonepe.platform.columbus.test.extensions.ElasticContainerExtension;
import io.dropwizard.setup.Environment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WithToolkit.All
@WithElasticContainer.Root
@WithMockDropwizardEnvironment
class ElasticClientInitializerTest {

    private static final VersionCommand VERSION = VersionCommand.builder().build();

    @Test
    @DisplayName("validate initialization step by step")
    void testInitializeInSteps(
            final Toolkit toolkit,
            final ElasticsearchContainer elastic,
            final Environment environment
    ) {
        final var configuration = ElasticConfiguration.builder()
                .enabled(true)
                .name("elastic-test")
                .hosts(HostConfiguration.builder()
                        .endpoint(Endpoint.builder()
                                .host(elastic.getHost())
                                .port(elastic.getMappedPort(ElasticContainerExtension.ELASTIC_SEARCH_PORT))
                                .build())
                        .build())
                .build();

        final var initializer = new DefaultElasticClientInitializer();

        assertFalse(initializer.getRestClient().isPresent(), "rest client cannot be initialized");
        assertFalse(initializer.getElasticClient().isPresent(), "client cannot be initialized");
        initializer.use(StorageSettings.defaults());
        assertFalse(initializer.getElasticClient().isPresent(), "client cannot be initialized");
        initializer.use(toolkit);
        assertFalse(initializer.getElasticClient().isPresent(), "client cannot be initialized");
        initializer.use(configuration);
        assertFalse(initializer.getElasticClient().isPresent(), "client cannot be initialized");
        initializer.use(environment);
        assertTrue(initializer.getElasticClient().isPresent(), "client can be initialized");

        final var storage = initializer.get();
        final var version = VERSION.execute(storage);

        assertEquals(ElasticContainerExtension.DEFAULT_ELASTIC_SEARCH_VERSION, version, "version must match");
    }
}
