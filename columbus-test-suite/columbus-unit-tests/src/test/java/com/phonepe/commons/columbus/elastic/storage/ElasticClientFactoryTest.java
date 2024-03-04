package com.phonepe.commons.columbus.elastic.storage;

import com.phonepe.commons.columbus.common.http.Endpoint;
import com.phonepe.commons.columbus.elastic.internal.storage.ColumbusElasticClient;
import com.phonepe.commons.columbus.elastic.storage.configuration.ElasticConfiguration;
import com.phonepe.commons.columbus.elastic.storage.configuration.HostConfiguration;
import com.phonepe.commons.columbus.internal.storage.factory.ColumbusStorageClientFactory;
import com.phonepe.commons.columbus.storage.commands.VersionCommand;
import com.phonepe.commons.columbus.test.annotations.WithElasticContainer;
import com.phonepe.commons.columbus.test.annotations.WithMockito;
import com.phonepe.commons.columbus.test.annotations.WithStorageFactory;
import com.phonepe.commons.columbus.test.extensions.ElasticContainerExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@WithMockito
@WithStorageFactory.All
@WithElasticContainer.Root
class ElasticClientFactoryTest {

    private static final VersionCommand VERSION = VersionCommand.builder().build();

    @Test
    @DisplayName("create client using factory")
    void testClientFactory(final ColumbusStorageClientFactory factory, final ElasticsearchContainer elastic) {
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
