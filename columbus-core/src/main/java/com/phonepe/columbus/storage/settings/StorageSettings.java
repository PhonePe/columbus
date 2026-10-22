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
package com.phonepe.columbus.storage.settings;

import com.phonepe.columbus.core.settings.RegistrySettings;
import com.phonepe.columbus.storage.StorageConfiguration;
import com.phonepe.columbus.storage.factory.visitor.StorageClientFactoryContext;
import com.phonepe.columbus.storage.factory.visitor.StorageClientProvider;
import lombok.Data;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.Map;

/**
 * A set of parameters to configure storages and their creation.
 */
@Data
@SuperBuilder(toBuilder = true)
public class StorageSettings {

    private final RegistrySettings visitorOverrides;

    private final RegistrySettings commandHandlerOverrides;

    @Singular(ignoreNullCollections = true)
    private final Map<String, StorageClientProvider<
            ? extends StorageConfiguration,
            ? extends StorageClientFactoryContext<?>>> nameOverrides;

    public static StorageSettings defaults() {
        return builder().build();
    }
}
