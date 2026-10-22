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
package com.phonepe.columbus.storage;

import com.phonepe.columbus.command.Command;
import com.phonepe.columbus.command.impl.CommandHandler;

/**
 * An interface responsible for handling a {@link Command} in the context of a {@link StorageClient}. Each
 * {@link Command} that is supported by a {@link StorageClient} is expected to have a class implementing this
 * interface, to execute it.
 *
 * @param <R> the type of the result.
 * @param <C> the type of the command.
 * @param <E> the type of the storage client.
 */
public interface StorageCommandHandler<R, C extends Command<R>, E extends StorageClient>
        extends CommandHandler<R, C, E> {

    @Override
    R handle(C command, E storage);
}
