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

import com.phonepe.columbus.core.initializer.ColumbusInitializer;
import com.phonepe.columbus.core.settings.ColumbusSettings;
import com.phonepe.columbus.dropwizard.internal.initializer.DropwizardColumbusInitializer;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Objects;
import java.util.function.Function;

/**
 * An extension to {@link ColumbusSettings} that includes an initializer and supports converting to a bundle.
 */
@Data
@SuperBuilder(toBuilder = true)
public class ColumbusBundleSettings extends ColumbusSettings {

    private final ColumbusInitializer columbusInitializer;

    public final <T> ColumbusBundle<T> toBundle(final Function<T, ColumbusBundleConfiguration> configurationExtractor) {
        return new ColumbusBundle<>(this, initializeAndGet()) {
            @Override
            protected ColumbusBundleConfiguration getColumbusConfiguration(final T configuration) {
                return configurationExtractor.apply(configuration);
            }
        };
    }

    private ColumbusInitializer initializeAndGet() {
        final var initializer = Objects.requireNonNullElseGet(columbusInitializer, DropwizardColumbusInitializer::new);
        initializer.use(this);
        return initializer;
    }
}
