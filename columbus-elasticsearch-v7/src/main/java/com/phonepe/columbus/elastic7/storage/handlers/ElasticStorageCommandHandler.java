package com.phonepe.columbus.elastic7.storage.handlers;

import com.phonepe.columbus.command.Command;
import com.phonepe.columbus.elastic7.storage.ElasticClient;
import com.phonepe.columbus.storage.StorageCommandHandler;

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
