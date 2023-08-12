package com.phonepe.platform.columbus.test.extensions;

import com.phonepe.platform.columbus.reflect.Generics;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import javax.annotation.Nullable;
import java.lang.reflect.Type;

public abstract class ValueExtension<T> implements AfterAllCallback, BeforeAllCallback, ParameterResolver {

    private static final Namespace NAMESPACE = Namespace.create(ValueExtension.class);

    private final Type supportedParameterType = getTypeParameter();

    @Override
    public void afterAll(final ExtensionContext context) {
        if (shouldCleanUp(context)) {
            cleanUp(context);
        }
    }

    @Override
    public void beforeAll(final ExtensionContext context) {
        getStore(context).getOrComputeIfAbsent(getKey(context), key -> initialize(context));
    }

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return Generics.isSuperType(parameterContext.getParameter().getParameterizedType(), supportedParameterType);
    }

    @Override
    public T resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return getValue(extensionContext, parameterContext);
    }

    protected abstract T initialize(final ExtensionContext context);

    protected abstract Class<T> getType();

    protected abstract boolean shouldUseRoot(final ExtensionContext context);

    protected abstract Object getKey(final ExtensionContext context, @Nullable final ParameterContext parameterContext);

    protected abstract boolean shouldCleanUp(final ExtensionContext context);

    protected Object getKey(final ExtensionContext context) {
        return getKey(context, null);
    }

    protected T getValue(final ExtensionContext context, @Nullable final ParameterContext parameterContext) {
        return getValueStore(context).get(getKey(context, parameterContext), getType());
    }

    protected Store getStore(final ExtensionContext context) {
        return getValueStore(shouldUseRoot(context) ? context.getRoot() : context);
    }

    protected Store getValueStore(final ExtensionContext context) {
        return context.getStore(getNamespace());
    }

    protected Namespace getNamespace() {
        return NAMESPACE;
    }

    protected void cleanUp(final ExtensionContext context) {
        getStore(context).remove(getKey(context), getType());
    }

    private Type getTypeParameter() {
        return Generics.getTypeParameter(getClass(), ValueExtension.class.getTypeParameters()[0]);
    }
}
