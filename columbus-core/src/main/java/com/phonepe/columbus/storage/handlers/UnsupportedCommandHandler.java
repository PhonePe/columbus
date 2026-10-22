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
package com.phonepe.columbus.storage.handlers;

import com.phonepe.columbus.command.Command;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.StorageClient;
import com.phonepe.columbus.storage.StorageCommandHandler;

/**
 * A generic {@link StorageCommandHandler} to raise an error if an unsupported {@link Command} is provided to the
 * {@link StorageClient}.
 *
 * @param <R> the type of the result.
 * @param <C> the type of the command.
 * @param <E> the type of the storage client.
 */
@RegistryKey(type = Command.class)
public class UnsupportedCommandHandler<R, C extends Command<R>, E extends StorageClient>
        implements StorageCommandHandler<R, C, E> {

    @Override
    public R handle(final C command, final E storage) {
        throw new UnsupportedOperationException(
                "Command '%s' is not supported by '%s'".formatted(command.name(), storage.name()));
    }
}
