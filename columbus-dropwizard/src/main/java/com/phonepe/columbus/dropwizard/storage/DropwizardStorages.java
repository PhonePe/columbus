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
package com.phonepe.columbus.dropwizard.storage;

import com.phonepe.columbus.core.toolkit.Toolkit;
import com.phonepe.columbus.dropwizard.internal.storage.factory.DropwizardStorageClientFactory;
import com.phonepe.columbus.storage.Storages;
import com.phonepe.columbus.storage.settings.StorageSettings;
import io.dropwizard.setup.Environment;
import lombok.experimental.UtilityClass;

/**
 * A collection of helper methods to create {@link com.phonepe.columbus.storage.StorageClient StorageClient}s
 * and their factories.
 */
@UtilityClass
public class DropwizardStorages {

    public DropwizardStorageClientFactory storageClientFactory(
            final StorageSettings settings,
            final Toolkit toolkit,
            final Environment environment
    ) {
        return DropwizardStorageClientFactory.builder()
                .resolver(Storages.storageProviderResolver(DropwizardStorageClientFactory.class, toolkit, settings))
                .toolkit(toolkit)
                .settings(settings)
                .dropwizardEnvironment(environment)
                .build();
    }
}
