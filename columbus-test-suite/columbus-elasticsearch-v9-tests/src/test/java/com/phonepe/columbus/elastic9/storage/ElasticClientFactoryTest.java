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

import com.phonepe.columbus.common.http.Endpoint;
import com.phonepe.columbus.elastic9.internal.storage.ColumbusElasticClient;
import com.phonepe.columbus.elastic9.storage.configuration.ElasticConfiguration;
import com.phonepe.columbus.elastic9.storage.configuration.HostConfiguration;
import com.phonepe.columbus.elastic9.storage.configuration.auth.AuthConfiguration;
import com.phonepe.columbus.elastic9.storage.configuration.auth.Credentials;
import com.phonepe.columbus.elastic9.test.annotations.WithElasticContainer;
import com.phonepe.columbus.elastic9.test.extensions.ElasticContainerExtension;
import com.phonepe.columbus.internal.storage.factory.ColumbusStorageClientFactory;
import com.phonepe.columbus.storage.commands.VersionCommand;
import com.phonepe.columbus.test.annotations.WithMockito;
import com.phonepe.columbus.test.annotations.WithStorageFactory;
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
