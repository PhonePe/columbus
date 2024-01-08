package com.phonepe.commons.columbus.elastic.storage;

import com.phonepe.commons.columbus.common.http.Header;
import com.phonepe.commons.columbus.elastic.internal.storage.ColumbusElasticClient;
import com.phonepe.commons.columbus.elastic.storage.configuration.ElasticConfiguration;
import com.phonepe.commons.columbus.elastic.storage.configuration.HostConfiguration;
import com.phonepe.commons.columbus.elastic.storage.configuration.RequestConfiguration;
import com.phonepe.commons.columbus.elastic.storage.configuration.auth.AuthConfiguration;
import com.phonepe.commons.columbus.elastic.storage.configuration.auth.Credentials;
import com.phonepe.commons.columbus.storage.commands.VersionCommand;
import com.phonepe.commons.columbus.test.annotations.WithDropwizardStorageFactory;
import com.phonepe.commons.columbus.test.annotations.WithElasticContainer;
import com.phonepe.commons.columbus.test.extensions.ElasticClientExtension;
import com.phonepe.commons.columbus.test.extensions.ElasticContainerExtension;
import com.phonepe.commons.columbus.test.value.ValueProps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WithDropwizardStorageFactory.All
@WithElasticContainer(
        value = @ValueProps(key = @ValueProps.Key(keyClass = ElasticClientTest.class)),
        password = ElasticClientTest.PASSWORD
)
class ElasticClientTest {

    public static final String USERNAME = "elastic";
    public static final String PASSWORD = "password";
    public static final String STORAGE_NAME = "elastic-test";

    @RegisterExtension
    static final ElasticClientExtension extension = ElasticClientExtension.builder()
            .configurationBuilder(ElasticConfiguration.builder()
                    .enabled(true)
                    .name(STORAGE_NAME)
                    .auth(AuthConfiguration.builder()
                            .credentials(Credentials.builder()
                                    .username(USERNAME)
                                    .password(PASSWORD)
                                    .build())
                            .build())
                    .requestConfig(RequestConfiguration.builder()
                            .defaultHeader(Header.builder()
                                    .name("X-Dummy-Header")
                                    .value("ignored")
                                    .build())
                            .systemPropertiesEnabled(true)
                            .build()))
            .hostBuilder(HostConfiguration.builder()
                    .compressionEnabled(true))
            .containerKey(ElasticClientTest.class)
            .build();
    private static final VersionCommand VERSION = VersionCommand.builder().build();

    @Test
    @DisplayName("create client with auth")
    void testCreateElasticClientWithAuth(final ColumbusElasticClient storage) {

        final var version = VERSION.execute(storage);

        assertEquals(ElasticContainerExtension.DEFAULT_ELASTIC_SEARCH_VERSION, version, "version must match");
        assertEquals(STORAGE_NAME, storage.name(), "name must match");
        assertEquals(ElasticConfiguration.TYPE, storage.type(), "type must match");
    }
}
