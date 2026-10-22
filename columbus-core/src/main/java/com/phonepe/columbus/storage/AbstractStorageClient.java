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

import com.phonepe.columbus.command.impl.CommandExecutor;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * An abstract implementation of {@link StorageClient} with a given {@link StorageConfiguration}, which incorporates
 * the {@link CommandExecutor} to execute the different storage commands.
 *
 * @param <C> the type of the configuration.
 * @param <E> the type of the storage client.
 */
@Getter
@SuperBuilder(toBuilder = true)
public abstract class AbstractStorageClient<C extends StorageConfiguration, E extends AbstractStorageClient<C, E>>
        extends CommandExecutor<E> implements StorageClient {

    @NonNull
    private final C configuration;

    @Override
    public @NonNull String type() {
        return configuration.getType();
    }

    @Override
    public @NonNull String name() {
        return configuration.getName();
    }
}
