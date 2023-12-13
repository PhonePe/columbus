package com.phonepe.platform.columbus.test.storage.memory.factory;

import com.phonepe.platform.columbus.command.CommandHelpers;
import com.phonepe.platform.columbus.core.utils.RegistryCreators;
import com.phonepe.platform.columbus.reflect.Generics;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.storage.StorageClient;
import com.phonepe.platform.columbus.storage.StorageCommandHandler;
import com.phonepe.platform.columbus.storage.factory.impl.ColumbusStorageFactoryContext;
import com.phonepe.platform.columbus.storage.factory.visitor.StorageClientProvider;
import com.phonepe.platform.columbus.test.storage.memory.MemoryStorage;
import com.phonepe.platform.columbus.test.storage.memory.MemoryStorageConfiguration;

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
