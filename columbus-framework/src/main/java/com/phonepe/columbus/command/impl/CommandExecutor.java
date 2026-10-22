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
package com.phonepe.columbus.command.impl;

import com.phonepe.columbus.command.Command;
import com.phonepe.columbus.command.Executor;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * The base executor includes all the contextual information needed to execute the given commands. It uses a
 * {@link HandlerResolver} to determine which {@link CommandHandler} should be used to execute the given command.
 * <p>
 * The resolver need not be strictly bound to the type of the executor. A resolver that works for the parent executor
 * can also work for the child.
 *
 * @param <E> the type of the executor.
 * @see HandlerResolver
 * @see CommandHandler
 */
@Getter
@SuperBuilder(toBuilder = true)
public abstract class CommandExecutor<E extends CommandExecutor<E>> implements Executor {

    @NonNull
    private final HandlerResolver<? super E> resolver;

    @Override
    public <R> R execute(final Command<R> command) {
        return resolver.handlerFor(command).handle(command, self());
    }

    @NonNull
    protected abstract E self();
}
