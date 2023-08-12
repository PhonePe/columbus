package com.phonepe.platform.columbus.dropwizard.storage.memory.factory;

import com.phonepe.platform.columbus.command.CommandHelpers;
import com.phonepe.platform.columbus.core.utils.RegistryCreators;
import com.phonepe.platform.columbus.dropwizard.storage.factory.DropwizardStorageFactoryContext;
import com.phonepe.platform.columbus.dropwizard.storage.memory.MemoryStorage;
import com.phonepe.platform.columbus.dropwizard.storage.memory.MemoryStorageConfiguration;
import com.phonepe.platform.columbus.reflect.Generics;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.storage.StorageClient;
import com.phonepe.platform.columbus.storage.StorageCommandHandler;
import com.phonepe.platform.columbus.storage.factory.visitor.StorageClientProvider;

@RegistryKey(name = MemoryStorageConfiguration.TYPE, type = MemoryStorageConfiguration.class)
public class MemoryStorageProvider<C extends DropwizardStorageFactoryContext<C>>
        implements StorageClientProvider<MemoryStorageConfiguration, C> {

    @Override
    public StorageClient visit(final MemoryStorageConfiguration configuration, final C context) {
        final var handlerRegistry = RegistryCreators.createHandlerRegistry(
                context.toolkit().handlerFactory(),
                Generics.typed(StorageCommandHandler.class),
                MemoryStorage.class,
                context.settings().getCommandHandlerOverrides());

        return MemoryStorage.builder()
                .resolver(CommandHelpers.resolver(handlerRegistry))
                .configuration(configuration)
                .environment(context.dropwizardEnvironment())
                .build();
    }
}
