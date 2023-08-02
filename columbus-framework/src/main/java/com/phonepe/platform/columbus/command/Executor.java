package com.phonepe.platform.columbus.command;

/**
 * An entity that can execute {@link Command}s. It stores the necessary contextual information and utilities that
 * allow the execution of commands seamlessly.
 *
 * @see Command
 */
public interface Executor {

    /**
     * Executes the given command.
     *
     * @param command the command to be executed.
     * @param <R>     represents the type of the result.
     * @return the result of the execution.
     * @throws IllegalArgumentException if the command is not supported, or has invalid data.
     * @throws NullPointerException     if the command is null and the executor does not support null commands.
     * @implNote Other {@link RuntimeException}s might be thrown depending on the implementation of the executor.
     */
    <R> R execute(Command<R> command);
}
