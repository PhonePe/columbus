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
package com.phonepe.columbus;

import com.phonepe.columbus.core.toolkit.Toolkit;
import com.phonepe.columbus.storage.StorageClient;

import java.util.Collection;

/**
 * A class that represents the runtime state of Columbus.
 */
public interface Columbus {

    /**
     * A toolkit is used to set up all the components of Columbus. It can be configured by providing a
     * {@link com.phonepe.columbus.core.toolkit.ToolkitSettings ToolkitSettings} to
     * {@link com.phonepe.columbus.core.settings.ColumbusSettings ColumbusSettings} during initialization.
     *
     * @return the initialized toolkit.
     */
    Toolkit toolkit();

    /**
     * All the storages that are enabled and initialized are registered here.
     *
     * @return a collection of initialized storages.
     */
    Collection<StorageClient> storages();

    /**
     * Get a storage client by its name.
     *
     * @param name the name of the storage to fetch.
     * @return the storage client with the given name, or null if no matching storage is found.
     */
    StorageClient storage(String name);
}
