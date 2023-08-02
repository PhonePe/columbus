package com.phonepe.platform.columbus.internal.command;

import com.phonepe.platform.columbus.command.Command;
import com.phonepe.platform.columbus.command.Executor;
import com.phonepe.platform.columbus.command.impl.CommandHandler;
import com.phonepe.platform.columbus.command.impl.HandlerResolver;
import com.phonepe.platform.columbus.registry.Registry;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * A handler resolver that uses a {@link Registry} to get a handler to execute the given command.
 */
@Getter
@SuperBuilder
public class HandlerRegistryResolver<E extends Executor> implements HandlerResolver<E> {

    @NonNull
    private final Registry<? super Command<?>, ? extends CommandHandler<?, ? extends Command<?>, ? super E>> registry;

    @Override
    public @NonNull <R, C extends Command<R>> CommandHandler<? extends R, ? super C, ? super E> handlerFor(
            final C command
    ) {
        return (CommandHandler<? extends R, ? super C, ? super E>) registry.get(command)
                .orElseThrow(() -> new IllegalArgumentException("No handler registered for command: " + command));
    }
}
