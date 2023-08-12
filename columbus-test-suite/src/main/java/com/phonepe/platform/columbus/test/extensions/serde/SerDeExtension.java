package com.phonepe.platform.columbus.test.extensions.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.platform.columbus.serde.SerDe;
import com.phonepe.platform.columbus.serde.SerDes;
import com.phonepe.platform.columbus.test.extensions.AnnotatedValueExtension;
import com.phonepe.platform.columbus.test.extensions.ValueProps;
import com.phonepe.platform.columbus.test.utils.AnnotatedValueUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Optional;

public class SerDeExtension extends AnnotatedValueExtension<SerDe> {

    @Override
    protected SerDe initialize(final ExtensionContext context) {
        final var key = getSerDeAnnotation(context).map(WithSerDe::mapper).orElse(null);
        final var store = getValueStore(context);
        return SerDes.json(AnnotatedValueUtils.getValue(store, key, ObjectMapper.class));
    }

    @Override
    protected Class<SerDe> getType() {
        return SerDe.class;
    }

    @Override
    protected Optional<ValueProps> getProps(final ExtensionContext context) {
        return getSerDeAnnotation(context).map(WithSerDe::value);
    }

    private static Optional<WithSerDe> getSerDeAnnotation(final ExtensionContext context) {
        return AnnotationSupport.findAnnotation(context.getTestClass(), WithSerDe.class);
    }
}
