package com.phonepe.platform.columbus;

import com.phonepe.platform.columbus.core.initializer.ColumbusInitializer;
import com.phonepe.platform.columbus.core.settings.ColumbusSettings;
import com.phonepe.platform.columbus.dropwizard.internal.initializer.DropwizardColumbusInitializer;
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
