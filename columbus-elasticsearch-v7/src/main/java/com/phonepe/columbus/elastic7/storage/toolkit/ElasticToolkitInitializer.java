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
package com.phonepe.columbus.elastic7.storage.toolkit;

import com.phonepe.columbus.core.toolkit.Toolkit;
import com.phonepe.columbus.elastic7.storage.configuration.ElasticConfiguration;
import com.phonepe.columbus.storage.settings.StorageSettings;

/**
 * An interface to initialize {@link ElasticToolkit} using the different, possibly optional, attributes it needs.
 *
 * @implSpec The attributes are provided as and when they are available, and the object is expected to be created
 * lazily on calling {@link #get()}.
 * @implNote Most likely, an instance of this class can only be used once due to the stateful nature.
 */
public interface ElasticToolkitInitializer {

    void use(Toolkit toolkit);

    void use(StorageSettings storageSettings);

    void use(ElasticConfiguration configuration);

    ElasticToolkit get();
}
