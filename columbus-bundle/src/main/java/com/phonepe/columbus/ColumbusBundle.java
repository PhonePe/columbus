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

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.columbus.common.initializer.LateInit;
import com.phonepe.columbus.core.initializer.ColumbusInitializer;
import com.phonepe.columbus.core.settings.ColumbusSettings;
import com.phonepe.columbus.dropwizard.initializer.DropwizardInitializer;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * A bundle to integrate Columbus into a Dropwizard application.
 * <p>
 * To instantiate the bundle, do:
 * <pre>
 * ColumbusBundle.configure()
 *     . // configure settings
 *     .build()
 *     .toBundle(** extract {@link ColumbusBundleConfiguration} from Dropwizard config **)
 * </pre>
 *
 * @param <T> the type of the configuration used by the Dropwizard application.
 */
@Slf4j
@RequiredArgsConstructor
public abstract class ColumbusBundle<T> implements ConfiguredBundle<T> {

    private final LateInit<ColumbusBundleConfiguration> configuration = new LateInit<>("configuration");
    private final LateInit<Columbus> columbus = new LateInit<>("Columbus");

    @Getter
    @NonNull
    private final ColumbusSettings settings;
    @NonNull
    private final ColumbusInitializer initializer;

    @SuppressWarnings({"java:S1452", "java:S3252"})
    public static ColumbusBundleSettings.ColumbusBundleSettingsBuilder<?, ?> configure() {
        return ColumbusBundleSettings.builder();
    }

    public ColumbusBundleConfiguration getConfiguration() {
        return configuration.get();
    }

    public Columbus columbus() {
        return columbus.get();
    }

    @Override
    public void run(final T configuration, final Environment environment) {
        log.info("Running Columbus Bundle");

        this.configuration.set(getColumbusConfiguration(configuration));

        final var bundleConfiguration = getConfiguration();

        if (!bundleConfiguration.isEnabled()) {
            log.warn("Columbus is not enabled!");
            return;
        }

        initializer.use(bundleConfiguration);
        if (initializer instanceof DropwizardInitializer dropwizardInitializer) {
            dropwizardInitializer.use(environment);
        }
        columbus.set(initializer.get());

        log.info("Columbus sets sail");
    }

    @Override
    public void initialize(final Bootstrap<?> bootstrap) {
        log.info("Initializing Columbus Bundle");

        final var mapper = bootstrap.getObjectMapper();
        initializer.use(mapper);
        mapper.registerSubtypes(initializer.toolkit().reflect().getAnnotatedTypes(JsonTypeName.class));

        log.info("Columbus Bundle initialized");
    }

    protected abstract ColumbusBundleConfiguration getColumbusConfiguration(T configuration);
}
