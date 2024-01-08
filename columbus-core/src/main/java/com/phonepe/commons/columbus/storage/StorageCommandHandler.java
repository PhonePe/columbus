package com.phonepe.commons.columbus.storage;

import com.phonepe.commons.columbus.command.Command;
import com.phonepe.commons.columbus.command.impl.CommandHandler;

/**
 * An interface responsible for handling a {@link Command} in the context of a {@link StorageClient}. Each
 * {@link Command} that is supported by a {@link StorageClient} is expected to have a class implementing this
 * interface, to execute it.
 *
 * @param <R> the type of the result.
 * @param <C> the type of the command.
 * @param <E> the type of the storage client.
 */
public interface StorageCommandHandler<R, C extends Command<R>, E extends StorageClient>
        extends CommandHandler<R, C, E> {

    @Override
    R handle(C command, E storage);
}
