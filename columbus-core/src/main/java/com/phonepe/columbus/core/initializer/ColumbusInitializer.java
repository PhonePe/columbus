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
package com.phonepe.columbus.core.initializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.columbus.Columbus;
import com.phonepe.columbus.core.settings.ColumbusConfiguration;
import com.phonepe.columbus.core.settings.ColumbusSettings;
import com.phonepe.columbus.core.toolkit.Toolkit;

/**
 * An interface to initialize {@link Columbus} using the different, possibly optional, attributes it needs.
 *
 * @implSpec The attributes are provided as and when they are available, and the object is expected to be created
 * lazily on calling {@link #get()}.
 * @implNote Most likely, an instance of this class can only be used once due to the stateful nature.
 */
public interface ColumbusInitializer {

    void use(ColumbusSettings settings);

    void use(ObjectMapper mapper);

    void use(ColumbusConfiguration configuration);

    Toolkit toolkit();

    Columbus get();
}
