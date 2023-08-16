package com.phonepe.platform.columbus.elastic.extensions.container;

import com.phonepe.platform.columbus.elastic.extensions.TestConstants;
import com.phonepe.platform.columbus.test.extensions.AnnotatedValueExtension;
import com.phonepe.platform.columbus.test.extensions.ValueProps;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;

public class ElasticContainerExtension extends AnnotatedValueExtension<ElasticsearchContainer> {

    @Override
    protected ElasticsearchContainer initialize(final ExtensionContext context) {
        final var annotation = getAnnotation(context);
        final var image = annotation.map(WithElasticContainer::image).orElse(TestConstants.ELASTIC_SEARCH_IMAGE);
        final var version = annotation.map(WithElasticContainer::version).orElse(TestConstants.ELASTIC_SEARCH_VERSION);
        final var elasticClientImage = DockerImageName.parse(image).withTag(version);
        final var container = new ElasticsearchContainer(elasticClientImage);
        annotation.map(WithElasticContainer::password)
                .filter(StringUtils::isNotBlank)
                .ifPresent(container::withPassword);
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
            container.stop();
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
