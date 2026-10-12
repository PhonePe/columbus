package com.phonepe.columbus.command;

/**
 * A command represents an operation, and all the data required to carry out the operation.
 * <p>
 * A command enforces the type of the result of the operation, but the {@link Executor} decides how the command is
 * executed.
 *
 * @param <R> the type of the result generated on executing the command.
 * @see Executor
 */
public interface Command<R> {

    String name();

    default R execute(Executor executor) {
        return executor.execute(this);
    }
}
