package com.phonepe.commons.columbus.internal.core.toolkit;

import com.phonepe.commons.columbus.command.CommandHelpers;
import com.phonepe.commons.columbus.command.factory.HandlerRegistryFactory;
import com.phonepe.commons.columbus.core.toolkit.Toolkit;
import com.phonepe.commons.columbus.reflect.Reflect;
import com.phonepe.commons.columbus.reflect.ReflectHelpers;
import com.phonepe.commons.columbus.reflect.ReflectSettings;
import com.phonepe.commons.columbus.registry.RegistryHelpers;
import com.phonepe.commons.columbus.registry.factory.RegistryFactory;
import com.phonepe.commons.columbus.serde.SerDe;
import com.phonepe.commons.columbus.serde.SerDes;
import com.phonepe.commons.columbus.type.Types;
import com.phonepe.commons.columbus.visitor.VisitorHelpers;
import com.phonepe.commons.columbus.visitor.factory.VisitorRegistryFactory;

import java.util.Objects;

/**
 * This class defines the default behaviour for creating the {@link Toolkit}.
 */
public class DefaultToolkitInitializer extends LazyToolkitInitializer {

    @Override
    protected boolean canInitializeReflect() {
        return getSettings().isPresent();
    }

    @Override
    protected Reflect initializeReflect() {
        final var settings = getSettings().get();
        return Objects.requireNonNullElseGet(settings.getReflectFactory(), ReflectHelpers::reflectFactory)
                .create(Objects.requireNonNullElseGet(settings.getReflectSettings(), ReflectSettings::defaults));
    }

    @Override
    protected boolean canInitializeSerDe() {
        return getMapper().isPresent();
    }

    @Override
    protected SerDe initializeSerDe() {
        final var mapper = getMapper().get();
        final var typeRegistry = getTypeRegistry();
        return typeRegistry.isPresent() && typeRegistry.get() != null
                ? SerDes.json(mapper, Types.resolver(typeRegistry.get()))
                : SerDes.json(mapper);
    }

    @Override
    protected boolean canInitializeRegistryFactory() {
        return getReflect().isPresent();
    }

    @Override
    protected RegistryFactory initializeRegistryFactory() {
        return RegistryHelpers.factory(getReflect().get());
    }

    @Override
    protected boolean canInitializeVisitorFactory() {
        return getRegistryFactory().isPresent();
    }

    @Override
    protected VisitorRegistryFactory initializeVisitorFactory() {
        return VisitorHelpers.factory(getRegistryFactory().get());
    }

    @Override
    protected boolean canInitializeHandlerFactory() {
        return getRegistryFactory().isPresent();
    }

    @Override
    protected HandlerRegistryFactory initializeHandlerFactory() {
        return CommandHelpers.factory(getRegistryFactory().get());
    }

    @Override
    protected boolean canInitializeToolkit() {
        return getReflect().isPresent()
                && getSerDe().isPresent()
                && getRegistryFactory().isPresent()
                && getVisitorFactory().isPresent()
                && getHandlerFactory().isPresent();
    }

    @Override
    protected Toolkit initializeToolkit() {
        return ToolkitData.builder()
                .reflect(getReflect().get())
                .serDe(getSerDe().get())
                .registryFactory(getRegistryFactory().get())
                .visitorFactory(getVisitorFactory().get())
                .handlerFactory(getHandlerFactory().get())
                .build();
    }
}
