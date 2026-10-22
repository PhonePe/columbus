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
package com.phonepe.columbus.test.storage.memory.handlers;

import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.commands.HealthCheckCommand;
import com.phonepe.columbus.storage.commands.HealthCheckCommand.HealthStatus;
import com.phonepe.columbus.test.storage.memory.MemoryStorage;

@RegistryKey(type = HealthCheckCommand.class)
public class HealthCheckHandler implements MemoryStorageCommandHandler<HealthStatus, HealthCheckCommand> {

    @Override
    public HealthStatus handle(final HealthCheckCommand command, final MemoryStorage storage) {
        return HealthStatus.HEALTHY;
    }
}
