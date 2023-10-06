package com.phonepe.platform.columbus.test.extensions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.platform.columbus.serde.SerDe;
import com.phonepe.platform.columbus.serde.SerDes;
import com.phonepe.platform.columbus.test.annotations.WithSerDe;
import com.phonepe.platform.columbus.test.value.AnnotatedValueExtension;
import com.phonepe.platform.columbus.test.value.ValueProps;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Optional;

import static com.phonepe.platform.columbus.test.utils.AnnotatedValueUtils.getRequiredValue;

public class SerDeExtension extends AnnotatedValueExtension<SerDe> {

    @Override
    protected SerDe initialize(final ExtensionContext context) {
        final var store = getValueStore(context);
        final var mapperProps = getAnnotation(context).map(WithSerDe::mapper).orElse(null);
        final var mapper = getRequiredValue(store, null, mapperProps, ObjectMapper.class);
        return SerDes.json(mapper);
    }

    @Override
    protected Class<SerDe> getType() {
        return SerDe.class;
    }

    @Override
    protected Optional<ValueProps> getProps(final ExtensionContext context) {
        return getAnnotation(context).map(WithSerDe::value);
    }

    private static Optional<WithSerDe> getAnnotation(final ExtensionContext context) {
        return AnnotationSupport.findAnnotation(context.getTestClass(), WithSerDe.class);
    }
}
