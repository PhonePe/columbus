package com.phonepe.columbus.dropwizard.storage;

import com.phonepe.columbus.core.toolkit.Toolkit;
import com.phonepe.columbus.dropwizard.internal.storage.factory.DropwizardStorageClientFactory;
import com.phonepe.columbus.storage.Storages;
import com.phonepe.columbus.storage.settings.StorageSettings;
import io.dropwizard.setup.Environment;
import lombok.experimental.UtilityClass;

/**
 * A collection of helper methods to create {@link com.phonepe.columbus.storage.StorageClient StorageClient}s
 * and their factories.
 */
@UtilityClass
public class DropwizardStorages {

    public DropwizardStorageClientFactory storageClientFactory(
            final StorageSettings settings,
            final Toolkit toolkit,
            final Environment environment
    ) {
        return DropwizardStorageClientFactory.builder()
                .resolver(Storages.storageProviderResolver(DropwizardStorageClientFactory.class, toolkit, settings))
                .toolkit(toolkit)
                .settings(settings)
                .dropwizardEnvironment(environment)
                .build();
    }
}
