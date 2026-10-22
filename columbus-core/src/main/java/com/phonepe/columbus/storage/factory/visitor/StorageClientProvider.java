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
import com.phonepe.columbus.visitor.impl.EntityVisitor;

/**
 * An interface to create a {@link StorageClient} from a {@link StorageConfiguration}. Each implementation of this
 * class is expected to handle a specific type of {@link StorageConfiguration} and create the corresponding
 * {@link StorageClient}.
 *
 * @param <T> the type of the storage configuration.
 * @param <C> the type of the context required by the implementer.
 * @implNote Each implementation can also specify the context class at the highest level that is needed to initialize
 * the client. Only the providers that can actually work with a subclass of {@link StorageClientFactoryContext} that is
 * provided will be used by the visitor.
 */
public interface StorageClientProvider<T extends StorageConfiguration, C extends StorageClientFactoryContext<C>>
        extends EntityVisitor<StorageClient, T, StorageConfiguration, Void, C> {

    @Override
    StorageClient visit(T configuration, Void props, C context);
}
