package com.phonepe.platform.columbus.storage.handlers;

import com.phonepe.platform.columbus.command.Command;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.storage.StorageClient;
import com.phonepe.platform.columbus.storage.StorageCommandHandler;

/**
 * A generic {@link StorageCommandHandler} to raise an error if an unsupported {@link Command} is provided to the
 * {@link StorageClient}.
 *
 * @param <R> the type of the result.
 * @param <C> the type of the command.
 * @param <E> the type of the storage client.
 */
@RegistryKey(type = Command.class)
public class UnsupportedCommandHandler<R, C extends Command<R>, E extends StorageClient>
        implements StorageCommandHandler<R, C, E> {

    @Override
    public R handle(final C command, final E storage) {
        throw new UnsupportedOperationException(
                "Command '%s' is not supported by '%s'".formatted(command.name(), storage.name()));
    }
}
