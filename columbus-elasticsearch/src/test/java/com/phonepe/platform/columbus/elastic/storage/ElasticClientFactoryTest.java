package com.phonepe.platform.columbus.elastic.storage;

import com.phonepe.platform.columbus.common.http.Endpoint;
import com.phonepe.platform.columbus.core.toolkit.Toolkit;
import com.phonepe.platform.columbus.dropwizard.storage.DropwizardStorages;
import com.phonepe.platform.columbus.elastic.extensions.TestConstants;
import com.phonepe.platform.columbus.elastic.extensions.container.WithElasticContainer;
import com.phonepe.platform.columbus.elastic.extensions.toolkit.WithToolkit;
import com.phonepe.platform.columbus.elastic.internal.storage.ColumbusElasticClient;
import com.phonepe.platform.columbus.elastic.storage.configuration.ElasticConfiguration;
import com.phonepe.platform.columbus.elastic.storage.configuration.HostConfiguration;
import com.phonepe.platform.columbus.storage.commands.VersionCommand;
import com.phonepe.platform.columbus.storage.settings.StorageSettings;
import com.phonepe.platform.columbus.test.extensions.WithMockito;
import io.dropwizard.setup.Environment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@WithMockito
@WithToolkit.All
@WithElasticContainer.Root
class ElasticClientFactoryTest {

    private static final VersionCommand VERSION = VersionCommand.builder().build();

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Environment environment;

    @Test
    @DisplayName("create client using factory")
    void testClientFactory(final Toolkit toolkit, final ElasticsearchContainer elastic) {
        final var factory = DropwizardStorages.storageClientFactory(StorageSettings.defaults(), toolkit, environment);

        final var configuration = ElasticConfiguration.builder()
                .enabled(true)
                .name("elastic-test")
                .hosts(HostConfiguration.builder()
                        .endpoint(Endpoint.builder()
                                .host(elastic.getHost())
                                .port(elastic.getMappedPort(TestConstants.ELASTIC_SEARCH_PORT))
                                .build())
                        .build())
                .build();

        final var storage = factory.create(configuration);

        assertAll(
                "ElasticClient",
                () -> assertNotNull(storage, "must create a valid storage"),
                () -> assertInstanceOf(ColumbusElasticClient.class, storage, "must be a ColumbusElasticClient"),
                () -> assertEquals(ElasticConfiguration.TYPE, storage.type(), "type must match"),
                () -> assertEquals(configuration.getName(), storage.name(), "name must match"),
                () -> assertEquals(TestConstants.ELASTIC_SEARCH_VERSION, VERSION.execute(storage),
                        "version must match"));
    }
}
