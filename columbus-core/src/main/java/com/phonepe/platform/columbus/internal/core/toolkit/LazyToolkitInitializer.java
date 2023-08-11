package com.phonepe.platform.columbus.internal.core.toolkit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.platform.columbus.command.factory.HandlerRegistryFactory;
import com.phonepe.platform.columbus.common.initializer.Initializer;
import com.phonepe.platform.columbus.common.initializer.LateInit;
import com.phonepe.platform.columbus.common.initializer.LazyInit;
import com.phonepe.platform.columbus.core.toolkit.Toolkit;
import com.phonepe.platform.columbus.core.toolkit.ToolkitInitializer;
import com.phonepe.platform.columbus.core.toolkit.ToolkitSettings;
import com.phonepe.platform.columbus.reflect.Reflect;
import com.phonepe.platform.columbus.registry.factory.RegistryFactory;
import com.phonepe.platform.columbus.serde.SerDe;
import com.phonepe.platform.columbus.type.TypeRegistry;
import com.phonepe.platform.columbus.visitor.factory.VisitorRegistryFactory;

/**
 * An object that uses the different {@link Initializer}s to create a dependency graph between the fields of
 * {@link Toolkit}. The preconditions and initializers are exposed to allow the children to define the behaviour for
 * each of the fields independently.
 *
 * @see DefaultToolkitInitializer
 */
public abstract class LazyToolkitInitializer implements ToolkitInitializer {

    private final LateInit<ObjectMapper> mapper = new LateInit<>("mapper");
    private final LateInit<TypeRegistry> typeRegistry = new LateInit<>("type registry");
    private final LateInit<ToolkitSettings> settings = new LateInit<>("settings");

    private final LazyInit<Reflect> reflect =
            new LazyInit<>("reflect", this::canInitializeReflect, this::initializeReflect);
    private final LazyInit<SerDe> serDe = new LazyInit<>("serDe", this::canInitializeSerDe, this::initializeSerDe);
    private final LazyInit<RegistryFactory> registryFactory =
            new LazyInit<>("registryFactory", this::canInitializeRegistryFactory, this::initializeRegistryFactory);
    private final LazyInit<VisitorRegistryFactory> visitorFactory =
            new LazyInit<>("visitorFactory", this::canInitializeVisitorFactory, this::initializeVisitorFactory);
    private final LazyInit<HandlerRegistryFactory> handlerFactory =
            new LazyInit<>("handlerFactory", this::canInitializeHandlerFactory, this::initializeHandlerFactory);
    private final LazyInit<Toolkit> toolkit =
            new LazyInit<>("toolkit", this::canInitializeToolkit, this::initializeToolkit);

    @Override
    public void use(final ObjectMapper mapper) {
        this.mapper.initialize(mapper);
    }

    @Override
    public void use(final TypeRegistry typeRegistry) {
        this.typeRegistry.initialize(typeRegistry);
    }

    @Override
    public void use(final ToolkitSettings settings) {
        this.settings.initialize(settings);
    }

    @Override
    public Toolkit get() {
        return getToolkit().get();
    }

    protected Initializer<ObjectMapper> getMapper() {
        return mapper;
    }

    protected Initializer<TypeRegistry> getTypeRegistry() {
        return typeRegistry;
    }

    protected Initializer<ToolkitSettings> getSettings() {
        return settings;
    }

    protected Initializer<Reflect> getReflect() {
        return reflect;
    }

    protected Initializer<SerDe> getSerDe() {
        return serDe;
    }

    protected Initializer<RegistryFactory> getRegistryFactory() {
        return registryFactory;
    }

    protected Initializer<VisitorRegistryFactory> getVisitorFactory() {
        return visitorFactory;
    }

    protected Initializer<HandlerRegistryFactory> getHandlerFactory() {
        return handlerFactory;
    }

    protected Initializer<Toolkit> getToolkit() {
        return toolkit;
    }

    protected abstract boolean canInitializeReflect();

    protected abstract Reflect initializeReflect();

    protected abstract boolean canInitializeSerDe();

    protected abstract SerDe initializeSerDe();

    protected abstract boolean canInitializeRegistryFactory();

    protected abstract RegistryFactory initializeRegistryFactory();

    protected abstract boolean canInitializeVisitorFactory();

    protected abstract VisitorRegistryFactory initializeVisitorFactory();

    protected abstract boolean canInitializeHandlerFactory();

    protected abstract HandlerRegistryFactory initializeHandlerFactory();

    protected abstract boolean canInitializeToolkit();

    protected abstract Toolkit initializeToolkit();
}
