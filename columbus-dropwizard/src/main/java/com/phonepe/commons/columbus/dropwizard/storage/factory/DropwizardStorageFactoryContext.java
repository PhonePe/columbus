package com.phonepe.commons.columbus.dropwizard.storage.factory;

import com.phonepe.commons.columbus.storage.factory.impl.ColumbusStorageFactoryContext;
import io.dropwizard.setup.Environment;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * A factory to create {@link com.phonepe.commons.columbus.storage.StorageClient StorageClient}s that utilize
 * Dropwizard features.
 *
 * @param <C> self reference, the context used by the providers.
 */
@Getter
@Accessors(fluent = true)
@SuperBuilder(toBuilder = true)
public abstract class DropwizardStorageFactoryContext<C extends DropwizardStorageFactoryContext<C>> extends
        ColumbusStorageFactoryContext<C> {

    @NonNull
    private final Environment dropwizardEnvironment;
}
