package com.phonepe.platform.columbus;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.common.initializer.LateInit;
import com.phonepe.platform.columbus.core.initializer.ColumbusInitializer;
import com.phonepe.platform.columbus.core.settings.ColumbusSettings;
import com.phonepe.platform.columbus.dropwizard.initializer.DropwizardInitializer;
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
        log.debug("Running Columbus Bundle");

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

        log.debug("Columbus sets sail");
    }

    @Override
    public void initialize(final Bootstrap<?> bootstrap) {
        log.debug("Initializing Columbus Bundle");

        final var mapper = bootstrap.getObjectMapper();
        initializer.use(mapper);
        mapper.registerSubtypes(initializer.toolkit().reflect().getAnnotatedTypes(JsonTypeName.class));

        log.debug("Columbus Bundle initialized");
    }

    protected abstract ColumbusBundleConfiguration getColumbusConfiguration(T configuration);
}
