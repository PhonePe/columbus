package com.phonepe.columbus.test.extensions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.columbus.core.toolkit.Toolkit;
import com.phonepe.columbus.core.toolkit.ToolkitHelpers;
import com.phonepe.columbus.core.toolkit.ToolkitSettings;
import com.phonepe.columbus.registry.Registries;
import com.phonepe.columbus.test.annotations.WithToolkit;
import com.phonepe.columbus.test.value.AnnotatedValueExtension;
import com.phonepe.columbus.test.value.ValueProps;
import com.phonepe.columbus.type.Types;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Optional;

import static com.phonepe.columbus.test.utils.AnnotatedValueUtils.getRequiredValue;

public class ToolkitExtension extends AnnotatedValueExtension<Toolkit> {

    @Override
    protected Toolkit initialize(final ExtensionContext context) {
        final var store = getValueStore(context);
        final var mapperProps = getAnnotation(context).map(WithToolkit::mapper).orElse(null);
        final var mapper = getRequiredValue(store, null, mapperProps, ObjectMapper.class);
        final var initializer = ToolkitHelpers.initializer();
        initializer.use(mapper);
        initializer.use(ToolkitSettings.defaults());
        initializer.use(Types.typeRegistry(Registries.empty()));
        return initializer.get();
    }

    @Override
    protected Class<Toolkit> getType() {
        return Toolkit.class;
    }

    @Override
    protected Optional<ValueProps> getProps(final ExtensionContext context) {
        return getAnnotation(context).map(WithToolkit::value);
    }

    private static Optional<WithToolkit> getAnnotation(final ExtensionContext context) {
        return AnnotationSupport.findAnnotation(context.getTestClass(), WithToolkit.class);
    }
}
