package com.phonepe.columbus.elastic9.internal.storage.factory.initializer;

import com.phonepe.columbus.common.http.Endpoint;
import com.phonepe.columbus.core.toolkit.Toolkit;
import com.phonepe.columbus.elastic9.internal.storage.toolkit.DefaultElasticToolkitInitializer;
import com.phonepe.columbus.elastic9.storage.configuration.ElasticConfiguration;
import com.phonepe.columbus.elastic9.storage.configuration.HostConfiguration;
import com.phonepe.columbus.elastic9.storage.configuration.auth.AuthConfiguration;
import com.phonepe.columbus.elastic9.storage.configuration.auth.Credentials;
import com.phonepe.columbus.elastic9.test.annotations.WithElasticContainer;
import com.phonepe.columbus.elastic9.test.extensions.ElasticContainerExtension;
import com.phonepe.columbus.storage.commands.VersionCommand;
import com.phonepe.columbus.storage.settings.StorageSettings;
import com.phonepe.columbus.test.annotations.WithMockDropwizardEnvironment;
import com.phonepe.columbus.test.annotations.WithToolkit;
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
class DropwizardElasticClientInitializerTest {

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
                .auth(AuthConfiguration.builder()
                        .credentials(Credentials.builder()
                                .username("elastic")
                                .password("changeme")
                                .build())
                        .build())
                .build();

        final var initializer = new DefaultDropwizardElasticClientInitializer();

        assertFalse(initializer.getRestClient().isPresent(), "rest client cannot be initialized");
        assertFalse(initializer.getElasticToolkit().isPresent(), "toolkit cannot be initialized");
        assertFalse(initializer.getElasticClient().isPresent(), "client cannot be initialized");
        initializer.use(StorageSettings.defaults());
        assertFalse(initializer.getElasticToolkit().isPresent(), "toolkit cannot be initialized");
        assertFalse(initializer.getElasticClient().isPresent(), "client cannot be initialized");
        initializer.use(toolkit);
        assertFalse(initializer.getElasticToolkit().isPresent(), "toolkit cannot be initialized");
        assertFalse(initializer.getElasticClient().isPresent(), "client cannot be initialized");
        initializer.use(configuration);
        assertFalse(initializer.getElasticClient().isPresent(), "client cannot be initialized");
        initializer.use(new DefaultElasticToolkitInitializer());
        assertTrue(initializer.getElasticToolkit().isPresent(), "toolkit can be initialized");
        assertFalse(initializer.getElasticClient().isPresent(), "client cannot be initialized");
        initializer.use(environment);
        assertTrue(initializer.getElasticClient().isPresent(), "client can be initialized");

        final var storage = initializer.get();
        final var version = VERSION.execute(storage);

        assertEquals(ElasticContainerExtension.DEFAULT_ELASTIC_SEARCH_VERSION, version, "version must match");
    }
}
