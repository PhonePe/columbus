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
