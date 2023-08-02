package com.phonepe.platform.columbus.command.impl;

import com.phonepe.platform.columbus.command.Command;
import com.phonepe.platform.columbus.command.Executor;
import lombok.NonNull;

/**
 * A resolver that determines which {@link CommandHandler} can be used to execute the given command.
 * <p>
 * The handlers need not be strictly bound to the type parameters of the resolver or the executor. The result is
 * covariant, while the command and the executor are contravariant.
 *
 * @param <E> the type of the executor.
 * @see CommandHandler
 */
public interface HandlerResolver<E extends Executor> {

    /**
     * Get the handler for the given command.
     *
     * @param command the command being executed.
     * @param <R>     the type of the result.
     * @param <C>     the type of the command.
     * @return the handler that can execute the given command.
     * @throws IllegalArgumentException if no handler can execute the given command.
     * @throws NullPointerException     if command is null and the executor does not support null values.
     */
    @SuppressWarnings("java:S1452")
    @NonNull <R, C extends Command<R>> CommandHandler<? extends R, ? super C, ? super E> handlerFor(C command);
}
