package com.phonepe.commons.columbus.storage;

import com.phonepe.commons.columbus.command.impl.CommandExecutor;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * An abstract implementation of {@link StorageClient} with a given {@link StorageConfiguration}, which incorporates
 * the {@link CommandExecutor} to execute the different storage commands.
 *
 * @param <C> the type of the configuration.
 * @param <E> the type of the storage client.
 */
@Getter
@SuperBuilder(toBuilder = true)
public abstract class AbstractStorageClient<C extends StorageConfiguration, E extends AbstractStorageClient<C, E>>
        extends CommandExecutor<E> implements StorageClient {

    @NonNull
    private final C configuration;

    @Override
    public @NonNull String type() {
        return configuration.getType();
    }

    @Override
    public @NonNull String name() {
        return configuration.getName();
    }
}
