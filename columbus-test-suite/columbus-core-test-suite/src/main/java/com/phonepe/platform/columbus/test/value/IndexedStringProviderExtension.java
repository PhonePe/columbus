package com.phonepe.platform.columbus.test.value;

import com.phonepe.platform.columbus.reflect.Generics;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class IndexedStringProviderExtension<T> implements ParameterResolver {

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return Generics.isSuperType(parameterContext.getParameter().getParameterizedType(), getType());
    }

    @Override
    public T resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
            throws ParameterResolutionException {
        final var format = getFormat(extensionContext);
        final var version = getStore(extensionContext)
                .getOrComputeIfAbsent(format, key -> new AtomicInteger(), AtomicInteger.class)
                .incrementAndGet();
        return createInstance(format.formatted(version));
    }

    protected abstract Class<T> getType();

    protected abstract T createInstance(String string);

    protected abstract Namespace getNamespace(ExtensionContext context);

    protected abstract String getFormat(ExtensionContext context);

    protected Store getStore(final ExtensionContext context) {
        return context.getRoot().getStore(getNamespace(context));
    }
}
