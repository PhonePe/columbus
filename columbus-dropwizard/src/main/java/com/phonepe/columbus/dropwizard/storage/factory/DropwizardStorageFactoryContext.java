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
package com.phonepe.columbus.dropwizard.storage.factory;

import com.phonepe.columbus.storage.factory.impl.ColumbusStorageFactoryContext;
import io.dropwizard.setup.Environment;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * A factory to create {@link com.phonepe.columbus.storage.StorageClient StorageClient}s that utilize
 * Dropwizard features.
 *
 * @param <C> self reference, the context used by the providers.
 */
@Getter
@Accessors(fluent = true)
@SuperBuilder(toBuilder = true)
public abstract class DropwizardStorageFactoryContext<C extends DropwizardStorageFactoryContext<C>> extends
        ColumbusStorageFactoryContext<C> {

    @NonNull
    private final Environment dropwizardEnvironment;
}
