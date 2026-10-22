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
package com.phonepe.columbus.storage.factory.visitor;

import com.phonepe.columbus.storage.StorageClient;
import com.phonepe.columbus.storage.StorageConfiguration;
import com.phonepe.columbus.storage.factory.StorageClientFactory;
import com.phonepe.columbus.visitor.impl.VisitorContext;
import lombok.experimental.SuperBuilder;

/**
 * A factory to create {@link StorageClient}s that uses {@link com.phonepe.columbus.visitor.Visitor Visitor}
 * construct. Each configuration is expected to have a {@link StorageClientProvider} to create a {@link StorageClient}.
 *
 * @param <C> self reference, the context used by the providers.
 * @implNote The subclasses can provide any additional functionality to be used by the {@link StorageClientProvider}.
 * @see StorageClientProvider
 */
@SuperBuilder(toBuilder = true)
public abstract class StorageClientFactoryContext<C extends StorageClientFactoryContext<C>>
        extends VisitorContext<StorageClient, StorageConfiguration, Void, C> implements StorageClientFactory {

    @Override
    public StorageClient create(final StorageConfiguration configuration) {
        return visit(configuration);
    }
}
