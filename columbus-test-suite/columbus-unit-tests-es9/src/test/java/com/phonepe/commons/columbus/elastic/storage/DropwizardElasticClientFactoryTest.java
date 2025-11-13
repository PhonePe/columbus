package com.phonepe.commons.columbus.elastic.storage;

import com.phonepe.commons.columbus.common.http.Endpoint;
import com.phonepe.commons.columbus.dropwizard.internal.storage.factory.DropwizardStorageClientFactory;
import com.phonepe.commons.columbus.elastic9.internal.storage.ColumbusElasticClient;
import com.phonepe.commons.columbus.elastic9.storage.configuration.ElasticConfiguration;
import com.phonepe.commons.columbus.elastic9.storage.configuration.HostConfiguration;
import com.phonepe.commons.columbus.elastic9.storage.configuration.auth.AuthConfiguration;
import com.phonepe.commons.columbus.elastic9.storage.configuration.auth.Credentials;
import com.phonepe.commons.columbus.storage.commands.VersionCommand;
import com.phonepe.commons.columbus.test.annotations.WithDropwizardStorageFactory;
import com.phonepe.commons.columbus.es9.test.annotations.WithElasticContainer;
import com.phonepe.commons.columbus.test.annotations.WithMockito;
import com.phonepe.commons.columbus.es9.test.extensions.ElasticContainerExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@WithMockito
@WithDropwizardStorageFactory.All
@WithElasticContainer.Root
class DropwizardElasticClientFactoryTest {

    private static final VersionCommand VERSION = VersionCommand.builder().build();

    @Test
    @DisplayName("create client using factory")
    void testClientFactory(final DropwizardStorageClientFactory factory, final ElasticsearchContainer elastic) {
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

        final var storage = factory.create(configuration);

        assertAll(
                "ElasticClient",
                () -> assertNotNull(storage, "must create a valid storage"),
                () -> assertInstanceOf(ColumbusElasticClient.class, storage, "must be a ColumbusElasticClient"),
                () -> assertEquals(ElasticConfiguration.TYPE, storage.type(), "type must match"),
                () -> assertEquals(configuration.getName(), storage.name(), "name must match"),
                () -> assertEquals(
                        ElasticContainerExtension.DEFAULT_ELASTIC_SEARCH_VERSION,
                        VERSION.execute(storage),
                        "version must match"));
    }
}
