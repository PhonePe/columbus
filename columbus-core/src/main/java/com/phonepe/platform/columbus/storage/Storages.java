package com.phonepe.platform.columbus.storage;

import com.phonepe.platform.columbus.common.pair.Pair;
import com.phonepe.platform.columbus.common.pair.PairFunctions;
import com.phonepe.platform.columbus.core.toolkit.Toolkit;
import com.phonepe.platform.columbus.core.utils.RegistryCreators;
import com.phonepe.platform.columbus.internal.storage.factory.ColumbusStorageClientFactory;
import com.phonepe.platform.columbus.internal.visitor.VisitorRegistryResolver;
import com.phonepe.platform.columbus.reflect.Generics;
import com.phonepe.platform.columbus.registry.Registries;
import com.phonepe.platform.columbus.registry.Registry;
import com.phonepe.platform.columbus.storage.factory.visitor.StorageClientFactoryContext;
import com.phonepe.platform.columbus.storage.factory.visitor.StorageClientProvider;
import com.phonepe.platform.columbus.storage.settings.StorageSettings;
import com.phonepe.platform.columbus.visitor.VisitorHelpers;
import lombok.experimental.UtilityClass;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A collection of helper methods to create {@link StorageClient}s and their factories.
 */
@UtilityClass
public class Storages {

    public ColumbusStorageClientFactory storageClientFactory(final StorageSettings settings, final Toolkit toolkit) {
        return ColumbusStorageClientFactory.builder()
                .resolver(storageProviderResolver(ColumbusStorageClientFactory.class, toolkit, settings))
                .toolkit(toolkit)
                .settings(settings)
                .build();
    }

    public <C extends StorageClientFactoryContext<C>>
    VisitorRegistryResolver<StorageClient, StorageConfiguration, Void, C> storageProviderResolver(
            final Class<C> factoryType,
            final Toolkit toolkit,
            final StorageSettings settings
    ) {
        return VisitorHelpers.resolver(storageProviderRegistry(factoryType, toolkit, settings));
    }

    @SuppressWarnings("unchecked")
    public <C extends StorageClientFactoryContext<C>>
    Registry<StorageConfiguration, StorageClientProvider<StorageConfiguration, C>> storageProviderRegistry(
            final Class<C> factoryType,
            final Toolkit toolkit,
            final StorageSettings settings
    ) {
        final var nameOverrides = settings.getNameOverrides().entrySet().stream()
                .map(entry -> Pair.of(entry.getKey(), entry.getValue()))
                .map(PairFunctions.transformingSecond(visitor -> {
                    if (VisitorHelpers.acceptsContext(Generics.typed(visitor.getClass()), factoryType)) {
                        return (StorageClientProvider<StorageConfiguration, C>) visitor;
                    }
                    return null;
                }))
                .filter(PairFunctions.filteringSecond(Objects::nonNull))
                .collect(Collectors.toMap(Pair::first, Pair::second));
        final var nameRegistry = Registries.transformingKey(
                StorageConfiguration::getName,
                Registries.withValues(nameOverrides));

        final var typeRegistry = RegistryCreators.createVisitorRegistry(
                toolkit.visitorFactory(),
                Generics.<StorageClientProvider<StorageConfiguration, C>>typed(StorageClientProvider.class),
                factoryType,
                StorageConfiguration.class,
                StorageConfiguration::getType,
                settings.getVisitorOverrides());

        return Registries.chaining(nameRegistry, typeRegistry).build();
    }
}
