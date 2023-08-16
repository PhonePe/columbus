package com.phonepe.platform.columbus.elastic.extensions.toolkit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.platform.columbus.core.toolkit.Toolkit;
import com.phonepe.platform.columbus.core.toolkit.ToolkitHelpers;
import com.phonepe.platform.columbus.core.toolkit.ToolkitSettings;
import com.phonepe.platform.columbus.test.extensions.AnnotatedValueExtension;
import com.phonepe.platform.columbus.test.extensions.ValueProps;
import com.phonepe.platform.columbus.test.utils.AnnotatedValueUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Objects;
import java.util.Optional;

public class ToolkitExtension extends AnnotatedValueExtension<Toolkit> {

    @Override
    protected Toolkit initialize(final ExtensionContext context) {
        final var key = getToolkitAnnotation(context).map(WithToolkit::mapper).orElse(null);
        final var store = getValueStore(context);
        final var mapper = Objects.requireNonNull(AnnotatedValueUtils.getValue(store, key, ObjectMapper.class),
                "No ObjectMapper provided");
        final var initializer = ToolkitHelpers.initializer();
        initializer.use(mapper);
        initializer.use(ToolkitSettings.defaults());
        return initializer.get();
    }

    @Override
    protected Class<Toolkit> getType() {
        return Toolkit.class;
    }

    @Override
    protected Optional<ValueProps> getProps(final ExtensionContext context) {
        return getToolkitAnnotation(context)
                .map(WithToolkit::value);
    }

    private static Optional<WithToolkit> getToolkitAnnotation(final ExtensionContext context) {
        return AnnotationSupport.findAnnotation(context.getTestClass(), WithToolkit.class);
    }
}
