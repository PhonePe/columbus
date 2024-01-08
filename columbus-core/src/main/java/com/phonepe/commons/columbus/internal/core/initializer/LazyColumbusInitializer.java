package com.phonepe.commons.columbus.internal.core.initializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.commons.columbus.Columbus;
import com.phonepe.commons.columbus.common.initializer.Initializer;
import com.phonepe.commons.columbus.common.initializer.LateInit;
import com.phonepe.commons.columbus.common.initializer.LazyInit;
import com.phonepe.commons.columbus.core.initializer.ColumbusInitializer;
import com.phonepe.commons.columbus.core.settings.ColumbusConfiguration;
import com.phonepe.commons.columbus.core.settings.ColumbusSettings;
import com.phonepe.commons.columbus.core.toolkit.Toolkit;
import com.phonepe.commons.columbus.core.toolkit.ToolkitInitializer;
import com.phonepe.commons.columbus.storage.StorageClient;
import com.phonepe.commons.columbus.storage.factory.StorageClientFactory;

import java.util.Map;

/**
 * An object that uses the different {@link Initializer}s to create a dependency graph between the fields of
 * {@link Columbus}. The preconditions and initializers are exposed to allow the children to define the behaviour for
 * each of the fields independently.
 *
 * @see DefaultColumbusInitializer
 */
public abstract class LazyColumbusInitializer implements ColumbusInitializer {

    private final LateInit<ObjectMapper> mapper = new LateInit<>("mapper");
    private final LateInit<ColumbusSettings> settings = new LateInit<>("settings");
    private final LateInit<ColumbusConfiguration> configuration = new LateInit<>("configuration");

    private final LazyInit<ToolkitInitializer> toolkitInitializer = new LazyInit<>(
            "toolkitInitializer", this::canInitializeToolkitInitializer, this::initializeToolkitInitializer);
    private final LazyInit<Toolkit> toolkit =
            new LazyInit<>("toolkit", this::canInitializeToolkit, this::initializeToolkit);
    private final LazyInit<StorageClientFactory> storageClientFactory = new LazyInit<>(
            "storageClientFactory", this::canInitializeStorageClientFactory, this::initializeStorageClientFactory);
    private final LazyInit<Map<String, StorageClient>> storages = new LazyInit<>(
            "storages", this::canInitializeStorages, this::initializeStorages);
    private final LazyInit<Columbus> columbus =
            new LazyInit<>("columbus", this::canInitializeColumbus, this::initializeColumbus);

    @Override
    public void use(final ColumbusSettings settings) {
        this.settings.initialize(settings);
    }

    @Override
    public void use(final ObjectMapper mapper) {
        this.mapper.set(mapper);
    }

    @Override
    public void use(final ColumbusConfiguration configuration) {
        this.configuration.set(configuration);
    }

    @Override
    public Toolkit toolkit() {
        return getToolkit().get();
    }

    @Override
    public Columbus get() {
        return getColumbus().get();
    }

    protected Initializer<ObjectMapper> getMapper() {
        return mapper;
    }

    protected Initializer<ColumbusSettings> getSettings() {
        return settings;
    }

    protected Initializer<ColumbusConfiguration> getConfiguration() {
        return configuration;
    }

    protected Initializer<ToolkitInitializer> getToolkitInitializer() {
        return toolkitInitializer;
    }

    protected Initializer<Toolkit> getToolkit() {
        return toolkit;
    }

    protected Initializer<StorageClientFactory> getStorageClientFactory() {
        return storageClientFactory;
    }

    protected Initializer<Map<String, StorageClient>> getStorages() {
        return storages;
    }

    protected Initializer<Columbus> getColumbus() {
        return columbus;
    }

    protected abstract boolean canInitializeToolkitInitializer();

    protected abstract ToolkitInitializer initializeToolkitInitializer();

    protected abstract boolean canInitializeToolkit();

    protected abstract Toolkit initializeToolkit();

    protected abstract boolean canInitializeStorageClientFactory();

    protected abstract StorageClientFactory initializeStorageClientFactory();

    protected abstract boolean canInitializeStorages();

    protected abstract Map<String, StorageClient> initializeStorages();

    protected abstract boolean canInitializeColumbus();

    protected abstract Columbus initializeColumbus();
}
