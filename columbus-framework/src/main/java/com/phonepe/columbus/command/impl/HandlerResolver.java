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
