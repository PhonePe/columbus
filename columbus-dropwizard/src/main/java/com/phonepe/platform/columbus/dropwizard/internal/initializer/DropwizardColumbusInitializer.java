package com.phonepe.platform.columbus.dropwizard.internal.initializer;

import com.phonepe.platform.columbus.common.initializer.Initializer;
import com.phonepe.platform.columbus.common.initializer.LateInit;
import com.phonepe.platform.columbus.dropwizard.initializer.DropwizardInitializer;
import com.phonepe.platform.columbus.dropwizard.storage.DropwizardStorages;
import com.phonepe.platform.columbus.internal.core.initializer.DefaultColumbusInitializer;
import com.phonepe.platform.columbus.storage.factory.StorageClientFactory;
import com.phonepe.platform.columbus.storage.settings.StorageSettings;
import io.dropwizard.setup.Environment;

import java.util.Objects;

/**
 * An object that configures Columbus initializer to utilize Dropwizard's features.
 */
public class DropwizardColumbusInitializer extends DefaultColumbusInitializer implements DropwizardInitializer {

    private final LateInit<Environment> environment = new LateInit<>("environment");

    @Override
    public void use(final Environment environment) {
        this.environment.set(environment);
    }

    public Initializer<Environment> getEnvironment() {
        return environment;
    }

    @Override
    protected boolean canInitializeStorageClientFactory() {
        return super.canInitializeStorageClientFactory() && getEnvironment().isPresent();
    }

    @Override
    protected StorageClientFactory initializeStorageClientFactory() {
        return DropwizardStorages.storageClientFactory(
                Objects.requireNonNullElseGet(getSettings().get().getStorageSettings(), StorageSettings::defaults),
                getToolkit().get(),
                getEnvironment().get());
    }
}
