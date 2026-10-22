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
