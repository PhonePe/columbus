package com.phonepe.columbus.internal.storage.factory;

import com.phonepe.columbus.storage.factory.impl.ColumbusStorageFactoryContext;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * A concrete implementation of {@link ColumbusStorageFactoryContext}.
 */
@SuperBuilder(toBuilder = true)
public final class ColumbusStorageClientFactory extends ColumbusStorageFactoryContext<ColumbusStorageClientFactory> {

    @Override
    protected @NonNull ColumbusStorageClientFactory self() {
        return this;
    }
}
