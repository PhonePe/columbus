package com.phonepe.platform.columbus.command.factory;

import com.phonepe.platform.columbus.command.Command;
import com.phonepe.platform.columbus.command.Executor;
import com.phonepe.platform.columbus.command.impl.CommandHandler;
import com.phonepe.platform.columbus.registry.factory.RegistryEntry;
import com.phonepe.platform.columbus.registry.factory.RegistryOptions;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.function.Function;

/**
 * A model with different parameters to help create a registry of {@link CommandHandler}s which can be used by an
 * {@link Executor} to execute {@link Command}s. The parameters can be tweaked to define which handlers are to be
 * registered, what data resolves to a handler, and how the handlers should be created.
 *
 * @param <K> the key type of the registry.
 * @param <R> the result type of the command.
 * @param <C> the type of the command.
 * @param <E> the type of the executor.
 * @param <H> the type of the command handler.
 */
@Data
@Accessors(fluent = true)
@SuperBuilder(toBuilder = true)
public class HandlerRegistryOptions<K, R, C extends Command<R>, E extends Executor, H extends CommandHandler<R, C, E>>
        extends RegistryOptions<K, H> {

    @NonNull
    private final Class<E> executorType;

    public static <K, R, C extends Command<R>, E extends Executor, H extends CommandHandler<R, C, E>>
    HandlerRegistryOptionsBuilder<K, R, C, E, H, ?, ?> builder(
            final Class<H> handlerType,
            final Class<E> executorType,
            final Function<RegistryEntry<? extends H>, K> keyExtractor
    ) {
        return new HandlerRegistryOptionsBuilderImpl<K, R, C, E, H>()
                .valueType(handlerType)
                .executorType(executorType)
                .keyExtractor(keyExtractor);
    }
}
