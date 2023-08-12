package com.phonepe.platform.columbus.dropwizard.internal.storage.factory;

import com.phonepe.platform.columbus.dropwizard.storage.factory.DropwizardStorageFactoryContext;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * A concrete implementation of {@link DropwizardStorageFactoryContext}.
 */
@SuperBuilder(toBuilder = true)
public final class DropwizardStorageClientFactory
        extends DropwizardStorageFactoryContext<DropwizardStorageClientFactory> {

    @Override
    protected @NonNull DropwizardStorageClientFactory self() {
        return this;
    }
}
