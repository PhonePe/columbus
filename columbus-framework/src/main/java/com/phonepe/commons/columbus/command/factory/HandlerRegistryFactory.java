package com.phonepe.commons.columbus.command.factory;

import com.phonepe.commons.columbus.command.Command;
import com.phonepe.commons.columbus.command.Executor;
import com.phonepe.commons.columbus.command.impl.CommandHandler;
import com.phonepe.commons.columbus.registry.Registry;

/**
 * A factory to create registry of {@link CommandHandler}s that can be used with an {@link Executor}.
 */
public interface HandlerRegistryFactory {

    <K, R, C extends Command<R>, E extends Executor, H extends CommandHandler<R, C, E>>
    Registry<K, H> create(final HandlerRegistryOptions<K, R, C, E, H> options);
}
