package com.phonepe.columbus.test.value;

import com.phonepe.columbus.test.utils.AnnotatedValueUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class AnnotatedValueExtension<T> extends ValueExtension<T> {

    @Override
    protected boolean shouldUseRoot(final ExtensionContext context) {
        return getProps(context)
                .map(ValueProps::useRootContext)
                .orElse(false);
    }

    @Override
    protected Object getKey(final ExtensionContext context, final @Nullable ParameterContext parameterContext) {
        return Optional.ofNullable(parameterContext)
                .flatMap(parameter -> parameter.findAnnotation(ValueProps.Key.class))
                .flatMap(AnnotatedValueUtils::getKey)
                .or(() -> getProps(context)
                        .map(ValueProps::key)
                        .flatMap(AnnotatedValueUtils::getKey))
                .orElseGet(this::getType);
    }

    @Override
    protected boolean shouldCleanUp(final ExtensionContext context) {
        return getProps(context)
                .map(ValueProps::cleanUp)
                .orElse(true);
    }

    protected abstract Optional<ValueProps> getProps(final ExtensionContext context);
}
