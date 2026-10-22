/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phonepe.columbus.command;

import com.phonepe.columbus.command.factory.HandlerRegistryOptions;
import com.phonepe.columbus.command.impl.CommandHandler;
import com.phonepe.columbus.internal.command.HandlerRegistryResolver;
import com.phonepe.columbus.internal.command.factory.DelegatingHandlerRegistryFactory;
import com.phonepe.columbus.reflect.Generics;
import com.phonepe.columbus.registry.Registry;
import com.phonepe.columbus.registry.factory.RegistryEntry;
import com.phonepe.columbus.registry.factory.RegistryFactory;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Predicate;

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

    public DelegatingHandlerRegistryFactory factory(final RegistryFactory registryFactory) {
        return DelegatingHandlerRegistryFactory.of(registryFactory);
    }

    @SuppressWarnings("java:S1452")
    public <R, C extends Command<R>, E extends Executor, H extends CommandHandler<R, C, E>>
    HandlerRegistryOptions.HandlerRegistryOptionsBuilder<String, R, C, E, H, ?, ?> optionsForName(
            final Class<H> baseHandlerType,
            final Class<E> executorType
    ) {
        return HandlerRegistryOptions.builder(baseHandlerType, executorType, RegistryEntry::name)
                .keyValidator(StringUtils::isNotEmpty);
    }

    @SuppressWarnings("java:S1452")
    public <R, C extends Command<R>, E extends Executor, H extends CommandHandler<R, C, E>>
    HandlerRegistryOptions.HandlerRegistryOptionsBuilder<Class<?>, R, C, E, H, ?, ?> optionsForType(
            final Class<H> baseHandlerType,
            final Class<E> executorType
    ) {
        return HandlerRegistryOptions.<Class<?>, R, C, E, H>builder(baseHandlerType, executorType, RegistryEntry::type)
                .keyValidator(Predicate.not(Void.class::equals));
    }

    public <R, C extends Command<R>, E extends Executor, H extends CommandHandler<R, C, E>>
    boolean acceptsExecutor(final Class<? extends H> handlerType, final Class<E> executorType) {
        final var parameter = Generics.getTypeParameter(handlerType, CommandHandler.class.getTypeParameters()[2]);
        return Generics.isSuperType(parameter, executorType);
    }
}
