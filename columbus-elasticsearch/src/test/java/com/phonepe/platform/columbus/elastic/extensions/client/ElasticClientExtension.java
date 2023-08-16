package com.phonepe.platform.columbus.elastic.extensions.client;

import com.phonepe.platform.columbus.common.http.Endpoint;
import com.phonepe.platform.columbus.core.toolkit.Toolkit;
import com.phonepe.platform.columbus.dropwizard.storage.DropwizardStorages;
import com.phonepe.platform.columbus.elastic.extensions.TestConstants;
import com.phonepe.platform.columbus.elastic.internal.storage.ColumbusElasticClient;
import com.phonepe.platform.columbus.elastic.storage.configuration.ElasticConfiguration;
import com.phonepe.platform.columbus.elastic.storage.configuration.HostConfiguration;
import com.phonepe.platform.columbus.storage.settings.StorageSettings;
import com.phonepe.platform.columbus.test.extensions.AnnotatedValueExtension;
import com.phonepe.platform.columbus.test.extensions.ValueProps;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import org.mockito.Mockito;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import java.util.Optional;

import static com.phonepe.platform.columbus.test.utils.AnnotatedValueUtils.getRequiredValue;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElasticClientExtension extends AnnotatedValueExtension<ColumbusElasticClient> {

    @NonNull
    @Builder.Default
    private final ElasticConfiguration.ElasticConfigurationBuilder<?, ?> configurationBuilder =
            ElasticConfiguration.builder()
                    .enabled(true)
                    .name("elastic-test");

    @NonNull
    @Builder.Default
    private final HostConfiguration.HostConfigurationBuilder hostBuilder = HostConfiguration.builder();

    @NonNull
    @Builder.Default
    private final StorageSettings storageSettings = StorageSettings.defaults();

    private Object toolkitKey;

    private Object containerKey;

    @Override
    protected ColumbusElasticClient initialize(final ExtensionContext context) {
        final var annotation = getAnnotation(context);
        final var store = getValueStore(context);
        final var toolkitProps = annotation.map(WithElasticClient::toolkit).orElse(null);
        final var toolkit = getRequiredValue(store, toolkitKey, toolkitProps, Toolkit.class);
        final var containerProps = annotation.map(WithElasticClient::container).orElse(null);
        final var container = getRequiredValue(store, containerKey, containerProps, ElasticsearchContainer.class);

        final var factory = DropwizardStorages.storageClientFactory(storageSettings, toolkit,
                Mockito.mock(Mockito.RETURNS_DEEP_STUBS));
        final var configuration = configurationBuilder.hosts(hostBuilder.clearEndpoints()
                        .endpoint(Endpoint.builder()
                                .host(container.getHost())
                                .port(container.getMappedPort(TestConstants.ELASTIC_SEARCH_PORT))
                                .build())
                        .build())
                .build();
        return (ColumbusElasticClient) factory.create(configuration);
    }

    @Override
    protected Class<ColumbusElasticClient> getType() {
        return ColumbusElasticClient.class;
    }

    @Override
    protected Optional<ValueProps> getProps(final ExtensionContext context) {
        return getAnnotation(context).map(WithElasticClient::value);
    }

    private static Optional<WithElasticClient> getAnnotation(final ExtensionContext context) {
        return AnnotationSupport.findAnnotation(context.getTestClass(), WithElasticClient.class);
    }
}
