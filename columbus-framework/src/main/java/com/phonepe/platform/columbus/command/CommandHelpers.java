package com.phonepe.platform.columbus.command;

import com.phonepe.platform.columbus.command.impl.CommandHandler;
import com.phonepe.platform.columbus.internal.command.HandlerRegistryResolver;
import com.phonepe.platform.columbus.registry.Registry;
import lombok.experimental.UtilityClass;

/**
 * A collection of helper methods to create different instances required to create an {@link Executor}.
 */
@UtilityClass
public class CommandHelpers {

    public <E extends Executor> HandlerRegistryResolver<E> resolver(
            final Registry<? super Command<?>, ? extends CommandHandler<?, ? extends Command<?>, ? super E>> registry
    ) {
        return HandlerRegistryResolver.<E>builder()
                .registry(registry)
                .build();
    }
}
