package com.phonepe.columbus.storage;

import com.phonepe.columbus.command.CommandHelpers;
import com.phonepe.columbus.registry.Registries;
import com.phonepe.columbus.storage.factory.impl.ColumbusStorageFactoryContext;
import com.phonepe.columbus.storage.factory.visitor.StorageClientProvider;
import com.phonepe.columbus.test.storage.memory.MemoryStorage;
import com.phonepe.columbus.test.storage.memory.MemoryStorageConfiguration;
import lombok.experimental.SuperBuilder;

interface DummyClientFactory {

    @SuperBuilder(toBuilder = true)
    abstract class DummyStorageFactoryContext<C extends DummyStorageFactoryContext<C>>
            extends ColumbusStorageFactoryContext<C> {}

    class DummyStorageProvider<C extends DummyStorageFactoryContext<C>>
            implements StorageClientProvider<MemoryStorageConfiguration, C> {

        @Override
        public StorageClient visit(final MemoryStorageConfiguration configuration, final Void props, final C context) {
            return MemoryStorage.builder()
                    .resolver(CommandHelpers.resolver(Registries.empty()))
                    .configuration(configuration)
                    .build();
        }
    }
}
