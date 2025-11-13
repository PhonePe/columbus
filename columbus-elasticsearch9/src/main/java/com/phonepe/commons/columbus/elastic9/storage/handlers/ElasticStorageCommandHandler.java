package com.phonepe.commons.columbus.elastic9.storage.handlers;

import com.phonepe.commons.columbus.command.Command;
import com.phonepe.commons.columbus.elastic9.storage.ElasticClient;
import com.phonepe.commons.columbus.storage.StorageCommandHandler;

/**
 * An abstract interface for handlers of all commands supported by the Elastic client.
 *
 * @param <R> the type of the result.
 * @param <C> the type of the command.
 * @param <E> the type of the Elastic client.
 */
public interface ElasticStorageCommandHandler<R, C extends Command<R>, E extends ElasticClient<E>>
        extends StorageCommandHandler<R, C, E> {
}
