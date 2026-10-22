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
package com.phonepe.columbus.dropwizard.initializer;

import com.phonepe.columbus.core.initializer.ColumbusInitializer;
import io.dropwizard.setup.Environment;

/**
 * An interface that adds Dropwizard's {@link Environment} to the initializer of
 * {@link com.phonepe.columbus.Columbus Columbus}.
 */
public interface DropwizardInitializer extends ColumbusInitializer {

    void use(Environment environment);
}
