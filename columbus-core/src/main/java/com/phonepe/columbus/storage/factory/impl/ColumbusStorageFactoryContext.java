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
package com.phonepe.columbus.storage.factory.impl;

import com.phonepe.columbus.core.toolkit.Toolkit;
import com.phonepe.columbus.storage.factory.visitor.StorageClientFactoryContext;
import com.phonepe.columbus.storage.settings.StorageSettings;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * A factory to create {@link com.phonepe.columbus.storage.StorageClient StorageClient}s that require Columbus
 * {@link Toolkit}.
 * <p>
 * This class can be extended to add more fields that are needed for the creation of clients.
 *
 * @param <C> self reference, the context used by the providers.
 * @see com.phonepe.columbus.internal.storage.factory.ColumbusStorageClientFactory ColumbusStorageClientFactory
 */
@Getter
@Accessors(fluent = true)
@SuperBuilder(toBuilder = true)
public abstract class ColumbusStorageFactoryContext<C extends ColumbusStorageFactoryContext<C>>
        extends StorageClientFactoryContext<C> {

    @NonNull
    private final Toolkit toolkit;

    @NonNull
    private final StorageSettings settings;
}
