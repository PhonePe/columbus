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
package com.phonepe.columbus.internal;

import com.phonepe.columbus.Columbus;
import com.phonepe.columbus.core.toolkit.Toolkit;
import com.phonepe.columbus.storage.StorageClient;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.Collection;
import java.util.Map;

/**
 * A concrete implementation of {@link Columbus} ensuring all the fields are initialized with valid values.
 */
@Data
@Accessors(fluent = true)
@SuperBuilder(toBuilder = true)
public class ColumbusData implements Columbus {

    @NonNull
    private final Toolkit toolkit;

    @Singular(ignoreNullCollections = true)
    private final Map<String, StorageClient> storages;

    @Override
    public Collection<StorageClient> storages() {
        return storages.values();
    }

    @Override
    public StorageClient storage(final String name) {
        return storages.get(name);
    }
}
