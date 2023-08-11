package com.phonepe.platform.columbus.storage;

import com.phonepe.platform.columbus.command.CommandHelpers;
import com.phonepe.platform.columbus.registry.Registries;
import com.phonepe.platform.columbus.storage.factory.impl.ColumbusStorageFactoryContext;
import com.phonepe.platform.columbus.storage.factory.visitor.StorageClientProvider;
import com.phonepe.platform.columbus.storage.memory.MemoryStorage;
import com.phonepe.platform.columbus.storage.memory.MemoryStorageConfiguration;
import lombok.experimental.SuperBuilder;

interface DummyClientFactory {

    @SuperBuilder(toBuilder = true)
    abstract class DummyStorageFactoryContext<C extends DummyStorageFactoryContext<C>>
            extends ColumbusStorageFactoryContext<C> {}

    class DummyStorageProvider<C extends DummyStorageFactoryContext<C>>
            implements StorageClientProvider<MemoryStorageConfiguration, C> {

        @Override
        public StorageClient visit(final MemoryStorageConfiguration configuration, final C context) {
            return MemoryStorage.builder()
                    .resolver(CommandHelpers.resolver(Registries.empty()))
                    .configuration(configuration)
                    .build();
        }
    }
}
