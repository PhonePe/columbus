package com.phonepe.commons.columbus.test.extensions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.commons.columbus.test.annotations.WithMapper;
import com.phonepe.commons.columbus.test.value.AnnotatedValueExtension;
import com.phonepe.commons.columbus.test.value.ValueProps;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Optional;

public class MapperExtension extends AnnotatedValueExtension<ObjectMapper> {

    @Override
    protected ObjectMapper initialize(final ExtensionContext context) {
        return new ObjectMapper();
    }

    @Override
    protected Class<ObjectMapper> getType() {
        return ObjectMapper.class;
    }

    @Override
    protected Optional<ValueProps> getProps(final ExtensionContext context) {
        return AnnotationSupport.findAnnotation(context.getTestClass(), WithMapper.class)
                .map(WithMapper::value);
    }
}
