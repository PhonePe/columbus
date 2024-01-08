package com.phonepe.commons.columbus.internal.core.initializer;

import com.phonepe.commons.columbus.Columbus;
import com.phonepe.commons.columbus.common.Predicates;
import com.phonepe.commons.columbus.core.toolkit.Toolkit;
import com.phonepe.commons.columbus.core.toolkit.ToolkitHelpers;
import com.phonepe.commons.columbus.core.toolkit.ToolkitInitializer;
import com.phonepe.commons.columbus.core.toolkit.ToolkitSettings;
import com.phonepe.commons.columbus.internal.ColumbusData;
import com.phonepe.commons.columbus.storage.StorageClient;
import com.phonepe.commons.columbus.storage.StorageConfiguration;
import com.phonepe.commons.columbus.storage.Storages;
import com.phonepe.commons.columbus.storage.factory.StorageClientFactory;
import com.phonepe.commons.columbus.storage.settings.StorageSettings;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class defines the default behaviour for creating the {@link Columbus}.
 */
public class DefaultColumbusInitializer extends LazyColumbusInitializer {

    @Override
    protected boolean canInitializeToolkitInitializer() {
        return getSettings().isPresent();
    }

    @Override
    protected ToolkitInitializer initializeToolkitInitializer() {
        final var settings = getSettings().get();
        final var initializer = Objects.requireNonNullElseGet(
                settings.getToolkitInitializer(),
                ToolkitHelpers::initializer);
        initializer.use(Objects.requireNonNullElseGet(settings.getToolkitSettings(), ToolkitSettings::defaults));
        Predicates.applyIfNotNull(settings.getTypeRegistry(), initializer::use);
        return initializer;
    }

    @Override
    protected boolean canInitializeToolkit() {
        return getToolkitInitializer().isPresent() && getMapper().isPresent();
    }

    @Override
    protected Toolkit initializeToolkit() {
        final var initializer = getToolkitInitializer().get();
        initializer.use(getMapper().get());
        return initializer.get();
    }

    @Override
    protected boolean canInitializeStorageClientFactory() {
        return getSettings().isPresent() && getToolkit().isPresent();
    }

    @Override
    protected StorageClientFactory initializeStorageClientFactory() {
        return Storages.storageClientFactory(
                Objects.requireNonNullElseGet(getSettings().get().getStorageSettings(), StorageSettings::defaults),
                getToolkit().get());
    }

    @Override
    protected boolean canInitializeStorages() {
        return getStorageClientFactory().isPresent() && getConfiguration().isPresent();
    }

    @Override
    protected Map<String, StorageClient> initializeStorages() {
        final var configuration = getConfiguration().get();
        if (!configuration.isEnabled()) {
            return Map.of();
        }
        final var factory = getStorageClientFactory().get();
        return configuration.getStorages().stream()
                .filter(StorageConfiguration::isEnabled)
                .map(factory::create)
                .collect(Collectors.toMap(StorageClient::name, Function.identity()));
    }

    @Override
    protected boolean canInitializeColumbus() {
        return getToolkit().isPresent() && getStorages().isPresent();
    }

    @Override
    protected Columbus initializeColumbus() {
        return ColumbusData.builder()
                .toolkit(getToolkit().get())
                .storages(Map.copyOf(getStorages().get()))
                .build();
    }
}
