/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phonepe.columbus.storage;

import com.phonepe.columbus.common.pair.Pair;
import com.phonepe.columbus.common.pair.PairFunctions;
import com.phonepe.columbus.core.toolkit.Toolkit;
import com.phonepe.columbus.core.utils.RegistryCreators;
import com.phonepe.columbus.internal.storage.factory.ColumbusStorageClientFactory;
import com.phonepe.columbus.internal.visitor.VisitorRegistryResolver;
import com.phonepe.columbus.reflect.Generics;
import com.phonepe.columbus.registry.Registries;
import com.phonepe.columbus.registry.Registry;
import com.phonepe.columbus.storage.factory.visitor.StorageClientFactoryContext;
import com.phonepe.columbus.storage.factory.visitor.StorageClientProvider;
import com.phonepe.columbus.storage.settings.StorageSettings;
import com.phonepe.columbus.visitor.VisitorHelpers;
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
