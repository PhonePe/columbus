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
package com.phonepe.columbus.internal.command;

import com.phonepe.columbus.command.Command;
import com.phonepe.columbus.command.Executor;
import com.phonepe.columbus.command.impl.CommandHandler;
import com.phonepe.columbus.command.impl.HandlerResolver;
import com.phonepe.columbus.registry.Registry;
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
