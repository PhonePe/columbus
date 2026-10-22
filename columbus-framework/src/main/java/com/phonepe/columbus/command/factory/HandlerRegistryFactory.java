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
package com.phonepe.columbus.command.factory;

import com.phonepe.columbus.command.Command;
import com.phonepe.columbus.command.Executor;
import com.phonepe.columbus.command.impl.CommandHandler;
import com.phonepe.columbus.registry.Registry;

/**
 * A factory to create registry of {@link CommandHandler}s that can be used with an {@link Executor}.
 */
public interface HandlerRegistryFactory {

    <K, R, C extends Command<R>, E extends Executor, H extends CommandHandler<R, C, E>>
    Registry<K, H> create(final HandlerRegistryOptions<K, R, C, E, H> options);
}
