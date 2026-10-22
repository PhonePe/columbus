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
package com.phonepe.columbus.test.storage.memory.factory;

import com.phonepe.columbus.command.CommandHelpers;
import com.phonepe.columbus.core.utils.RegistryCreators;
import com.phonepe.columbus.reflect.Generics;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.StorageClient;
import com.phonepe.columbus.storage.StorageCommandHandler;
import com.phonepe.columbus.storage.factory.impl.ColumbusStorageFactoryContext;
import com.phonepe.columbus.storage.factory.visitor.StorageClientProvider;
import com.phonepe.columbus.test.storage.memory.MemoryStorage;
import com.phonepe.columbus.test.storage.memory.MemoryStorageConfiguration;

@RegistryKey(name = MemoryStorageConfiguration.TYPE, type = MemoryStorageConfiguration.class)
public class MemoryStorageProvider<C extends ColumbusStorageFactoryContext<C>>
        implements StorageClientProvider<MemoryStorageConfiguration, C> {

    @Override
    public StorageClient visit(final MemoryStorageConfiguration configuration, final Void props, final C context) {
        final var handlerRegistry = RegistryCreators.createHandlerRegistry(
                context.toolkit().handlerFactory(),
                Generics.typed(StorageCommandHandler.class),
                MemoryStorage.class,
                context.settings().getCommandHandlerOverrides());

        return MemoryStorage.builder()
                .resolver(CommandHelpers.resolver(handlerRegistry))
                .configuration(configuration)
                .build();
    }
}
