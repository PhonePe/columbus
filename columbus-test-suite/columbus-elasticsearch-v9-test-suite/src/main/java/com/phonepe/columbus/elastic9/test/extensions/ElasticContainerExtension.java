package com.phonepe.columbus.elastic9.test.extensions;

import com.phonepe.columbus.elastic9.test.annotations.WithElasticContainer;
import com.phonepe.columbus.test.value.AnnotatedValueExtension;
import com.phonepe.columbus.test.value.ValueProps;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;

public class ElasticContainerExtension extends AnnotatedValueExtension<ElasticsearchContainer> {

    public static final String DEFAULT_ELASTIC_SEARCH_IMAGE = "docker.elastic.co/elasticsearch/elasticsearch";
    public static final String DEFAULT_ELASTIC_SEARCH_VERSION = "9.1.2";
    public static final int ELASTIC_SEARCH_PORT = 9200;
    public static final String ELASTIC_SEARCH_DOCKER_IMAGE_ENV = "ELASTIC_SEARCH_9_DOCKER_IMAGE";
    private static final String FALSE = "false";

    @Override
    @SneakyThrows
    @SuppressWarnings("java:S2095")
    protected ElasticsearchContainer initialize(final ExtensionContext context) {
        final var annotation = getAnnotation(context);
        final var image = annotation.map(WithElasticContainer::imageEnvOverride)
                .filter(StringUtils::isNotBlank)
                .map(System::getenv)
                .filter(StringUtils::isNotBlank)
                .or(() -> annotation.map(WithElasticContainer::image)
                        .filter(StringUtils::isNotBlank))
                .orElse(DEFAULT_ELASTIC_SEARCH_IMAGE);
        final var version = annotation.map(WithElasticContainer::version)
                .filter(StringUtils::isNotBlank)
                .orElse(DEFAULT_ELASTIC_SEARCH_VERSION);
        final var elasticClientImage = DockerImageName.parse(image)
                .withTag(version)
                .asCompatibleSubstituteFor(DEFAULT_ELASTIC_SEARCH_IMAGE);
        final var container = new ElasticsearchContainer(elasticClientImage)
                .withEnv("ES_JAVA_OPTS", "-Xms512m -Xmx512m -XX:MaxDirectMemorySize=536870912");
        annotation.map(WithElasticContainer::password)
                .filter(StringUtils::isNotBlank)
                .ifPresent(container::withPassword);

        container.withEnv("xpack.security.enabled", FALSE);
        container.withEnv("xpack.security.http.ssl.enabled", FALSE);
        container.withEnv("xpack.security.transport.ssl.enabled", FALSE);
        container.start();
        return container;
    }

    @Override
    protected Class<ElasticsearchContainer> getType() {
        return ElasticsearchContainer.class;
    }

    @Override
    protected void cleanUp(final ExtensionContext context) {
        final var container = getStore(context).remove(getKey(context), getType());
        if (container != null) {
            container.close();
        }
    }

    @Override
    protected Optional<ValueProps> getProps(final ExtensionContext context) {
        return getAnnotation(context).map(WithElasticContainer::value);
    }

    private static Optional<WithElasticContainer> getAnnotation(final ExtensionContext context) {
        return AnnotationSupport.findAnnotation(context.getTestClass(), WithElasticContainer.class);
    }
}
