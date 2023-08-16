package com.phonepe.platform.columbus.elastic.storage;

import com.phonepe.platform.columbus.command.CommandHelpers;
import com.phonepe.platform.columbus.common.http.Endpoint;
import com.phonepe.platform.columbus.common.http.Header;
import com.phonepe.platform.columbus.core.toolkit.Toolkit;
import com.phonepe.platform.columbus.core.utils.RegistryCreators;
import com.phonepe.platform.columbus.elastic.extensions.TestConstants;
import com.phonepe.platform.columbus.elastic.extensions.container.WithElasticContainer;
import com.phonepe.platform.columbus.elastic.extensions.toolkit.WithToolkit;
import com.phonepe.platform.columbus.elastic.internal.storage.ColumbusElasticClient;
import com.phonepe.platform.columbus.elastic.storage.configuration.ElasticConfiguration;
import com.phonepe.platform.columbus.elastic.storage.configuration.HostConfiguration;
import com.phonepe.platform.columbus.elastic.storage.configuration.RequestConfiguration;
import com.phonepe.platform.columbus.elastic.storage.configuration.auth.AuthConfiguration;
import com.phonepe.platform.columbus.elastic.storage.utils.ElasticRestClientUtils;
import com.phonepe.platform.columbus.reflect.Generics;
import com.phonepe.platform.columbus.storage.StorageCommandHandler;
import com.phonepe.platform.columbus.storage.commands.VersionCommand;
import com.phonepe.platform.columbus.test.extensions.ValueProps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WithToolkit.All
@WithElasticContainer(value = @ValueProps(key = @ValueProps.Key(keyClass = ElasticClientTest.class)))
class ElasticClientTest {

    private static final VersionCommand VERSION = VersionCommand.builder().build();

    @Test
    @DisplayName("create client with auth")
    void testCreateElasticClientWithAuth(
            final Toolkit toolkit,
            @ValueProps.Key(keyClass = ElasticClientTest.class) final ElasticsearchContainer elastic
    ) {
        final var configuration = ElasticConfiguration.builder()
                .enabled(true)
                .name("elastic-test")
                .hosts(HostConfiguration.builder()
                        .endpoint(Endpoint.builder()
                                .host(elastic.getHost())
                                .port(elastic.getMappedPort(TestConstants.ELASTIC_SEARCH_PORT))
                                .build())
                        .compressionEnabled(true)
                        .build())
                .auth(AuthConfiguration.builder()
                        .credentials(TestConstants.ELASTIC_SEARCH_CREDENTIALS)
                        .build())
                .requestConfig(RequestConfiguration.builder()
                        .defaultHeader(Header.builder()
                                .name("X-Dummy-Header")
                                .value("ignored")
                                .build())
                        .systemPropertiesEnabled(true)
                        .build())
                .build();

        final var storage = ColumbusElasticClient.builder()
                .resolver(CommandHelpers.resolver(RegistryCreators.createHandlerRegistry(
                        toolkit.handlerFactory(),
                        Generics.typed(StorageCommandHandler.class),
                        ColumbusElasticClient.class,
                        null)))
                .client(ElasticRestClientUtils.createClient(configuration))
                .configuration(configuration)
                .build();

        final var version = VERSION.execute(storage);

        assertEquals(TestConstants.ELASTIC_SEARCH_VERSION, version, "version must match");
        assertEquals(configuration.getName(), storage.name(), "name must match");
        assertEquals(configuration.getType(), storage.type(), "type must match");
    }
}
