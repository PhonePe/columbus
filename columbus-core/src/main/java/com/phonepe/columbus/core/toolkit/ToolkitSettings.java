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
package com.phonepe.columbus.core.toolkit;

import com.phonepe.columbus.reflect.ReflectFactory;
import com.phonepe.columbus.reflect.ReflectSettings;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * A set of parameters to configure {@link Toolkit} and its fields as needed.
 */
@Data
@SuperBuilder(toBuilder = true)
public class ToolkitSettings {

    private final ReflectSettings reflectSettings;

    private final ReflectFactory reflectFactory;

    public static ToolkitSettings defaults() {
        return builder().build();
    }
}
