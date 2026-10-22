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
package com.phonepe.columbus.internal.command.factory;

import com.phonepe.columbus.command.Command;
import com.phonepe.columbus.command.CommandHelpers;
import com.phonepe.columbus.command.Executor;
import com.phonepe.columbus.command.factory.HandlerRegistryFactory;
import com.phonepe.columbus.command.factory.HandlerRegistryOptions;
import com.phonepe.columbus.command.impl.CommandHandler;
import com.phonepe.columbus.registry.Registry;
import com.phonepe.columbus.registry.factory.RegistryFactory;
import com.phonepe.columbus.registry.factory.RegistryOptions;
import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * A factory for creating a registry of {@link CommandHandler}s for a fixed {@link Executor}, that uses
 * {@link RegistryFactory}.
 */
@AllArgsConstructor(staticName = "of")
public class DelegatingHandlerRegistryFactory implements HandlerRegistryFactory {

    @NonNull
    private final RegistryFactory registryFactory;

    @Override
    public <K, R, C extends Command<R>, E extends Executor, H extends CommandHandler<R, C, E>>
    Registry<K, H> create(final HandlerRegistryOptions<K, R, C, E, H> options) {
        final var executorType = options.executorType();

        final var registryOptions = RegistryOptions.builder(options.valueType(), options.keyExtractor())
                .typeFilter(options.typeFilter().and(type -> CommandHelpers.acceptsExecutor(type, executorType)))
                .keyValidator(options.keyValidator())
                .ignorePriorityConflicts(options.ignorePriorityConflicts())
                .build();

        return registryFactory.create(registryOptions);
    }
}
