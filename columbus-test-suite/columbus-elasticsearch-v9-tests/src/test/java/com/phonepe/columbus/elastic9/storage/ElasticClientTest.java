/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phonepe.columbus.elastic9.storage;

import com.phonepe.columbus.common.http.Header;
import com.phonepe.columbus.elastic9.internal.storage.ColumbusElasticClient;
import com.phonepe.columbus.elastic9.storage.configuration.ElasticConfiguration;
import com.phonepe.columbus.elastic9.storage.configuration.HostConfiguration;
import com.phonepe.columbus.elastic9.storage.configuration.RequestConfiguration;
import com.phonepe.columbus.elastic9.test.annotations.WithElasticContainer;
import com.phonepe.columbus.elastic9.test.extensions.ElasticClientExtension;
import com.phonepe.columbus.elastic9.test.extensions.ElasticContainerExtension;
import com.phonepe.columbus.storage.commands.VersionCommand;
import com.phonepe.columbus.test.annotations.WithDropwizardStorageFactory;
import com.phonepe.columbus.test.value.ValueProps;
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
    public static final String PASSWORD = "changeme";
    public static final String STORAGE_NAME = "elastic-test";

    @RegisterExtension
    static final ElasticClientExtension extension = ElasticClientExtension.builder()
            .configurationBuilder(ElasticConfiguration.builder()
                    .enabled(true)
                    .name(STORAGE_NAME)
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
            .username(USERNAME)
            .password(PASSWORD)
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
