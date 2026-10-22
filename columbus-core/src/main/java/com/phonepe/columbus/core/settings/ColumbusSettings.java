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
package com.phonepe.columbus.core.settings;

import com.phonepe.columbus.core.toolkit.ToolkitInitializer;
import com.phonepe.columbus.core.toolkit.ToolkitSettings;
import com.phonepe.columbus.storage.settings.StorageSettings;
import com.phonepe.columbus.type.TypeRegistry;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * A set of parameters to configure {@link com.phonepe.columbus.Columbus Columbus}.
 *
 * @see ColumbusConfiguration
 */
@Data
@SuperBuilder(toBuilder = true)
public class ColumbusSettings {

    private final ToolkitSettings toolkitSettings;

    private final ToolkitInitializer toolkitInitializer;

    private final TypeRegistry typeRegistry;

    private final StorageSettings storageSettings;

    public static ColumbusSettings defaults() {
        return builder().build();
    }
}
