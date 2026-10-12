package com.phonepe.columbus.command.impl;

import com.phonepe.columbus.command.Command;
import com.phonepe.columbus.command.Executor;

/**
 * A command handler is responsible for performing the operation defined by the command, with the given executor.
 *
 * @param <R> the type of the result.
 * @param <C> the type of the command.
 * @param <E> the type of the executor.
 */
public interface CommandHandler<R, C extends Command<R>, E extends Executor> {

    R handle(C command, E executor);
}
