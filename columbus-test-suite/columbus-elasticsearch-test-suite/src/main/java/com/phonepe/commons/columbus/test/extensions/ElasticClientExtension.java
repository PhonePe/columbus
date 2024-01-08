package com.phonepe.commons.columbus.test.extensions;

import com.phonepe.commons.columbus.common.http.Endpoint;
import com.phonepe.commons.columbus.dropwizard.internal.storage.factory.DropwizardStorageClientFactory;
import com.phonepe.commons.columbus.elastic.internal.storage.ColumbusElasticClient;
import com.phonepe.commons.columbus.elastic.storage.configuration.ElasticConfiguration;
import com.phonepe.commons.columbus.elastic.storage.configuration.HostConfiguration;
import com.phonepe.commons.columbus.storage.settings.StorageSettings;
import com.phonepe.commons.columbus.test.annotations.WithElasticClient;
import com.phonepe.commons.columbus.test.value.AnnotatedValueExtension;
import com.phonepe.commons.columbus.test.value.ValueProps;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import java.util.Optional;

import static com.phonepe.commons.columbus.test.utils.AnnotatedValueUtils.getRequiredValue;

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

    private Object factoryKey;

    private Object containerKey;

    @Override
    protected ColumbusElasticClient initialize(final ExtensionContext context) {
        final var annotation = getAnnotation(context);
        final var store = getValueStore(context);
        final var factoryProps = annotation.map(WithElasticClient::storageFactory).orElse(null);
        final var factory = getRequiredValue(store, factoryKey, factoryProps, DropwizardStorageClientFactory.class);
        final var containerProps = annotation.map(WithElasticClient::container).orElse(null);
        final var container = getRequiredValue(store, containerKey, containerProps, ElasticsearchContainer.class);

        final var configuration = configurationBuilder.hosts(hostBuilder.clearEndpoints()
                        .endpoint(Endpoint.builder()
                                .host(container.getHost())
                                .port(container.getMappedPort(ElasticContainerExtension.ELASTIC_SEARCH_PORT))
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
