package com.phonepe.columbus.elastic9.test.extensions;

import com.phonepe.columbus.core.toolkit.Toolkit;
import com.phonepe.columbus.elastic9.storage.toolkit.ElasticToolkit;
import com.phonepe.columbus.elastic9.storage.toolkit.ElasticToolkitHelpers;
import com.phonepe.columbus.elastic9.test.annotations.WithElasticToolkit;
import com.phonepe.columbus.test.value.AnnotatedValueExtension;
import com.phonepe.columbus.test.value.ValueProps;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Optional;

import static com.phonepe.columbus.test.utils.AnnotatedValueUtils.getRequiredValue;

public class ElasticToolkitExtension extends AnnotatedValueExtension<ElasticToolkit> {

    @Override
    protected ElasticToolkit initialize(final ExtensionContext context) {
        final var store = getValueStore(context);
        final var toolkitProps = getAnnotation(context).map(WithElasticToolkit::toolkit).orElse(null);
        final var toolkit = getRequiredValue(store, null, toolkitProps, Toolkit.class);
        return ElasticToolkitHelpers.defaultInstance(toolkit);
    }

    @Override
    protected Class<ElasticToolkit> getType() {
        return ElasticToolkit.class;
    }

    @Override
    protected Optional<ValueProps> getProps(final ExtensionContext context) {
        return getAnnotation(context).map(WithElasticToolkit::value);
    }

    private static Optional<WithElasticToolkit> getAnnotation(final ExtensionContext context) {
        return AnnotationSupport.findAnnotation(context.getTestClass(), WithElasticToolkit.class);
    }
}
