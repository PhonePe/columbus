package com.phonepe.platform.columbus.storage.memory.handlers;

import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.storage.commands.HealthCheckCommand;
import com.phonepe.platform.columbus.storage.commands.HealthCheckCommand.HealthStatus;
import com.phonepe.platform.columbus.storage.memory.MemoryStorage;

@RegistryKey(type = HealthCheckCommand.class)
public class HealthCheckHandler implements MemoryStorageCommandHandler<HealthStatus, HealthCheckCommand> {

    @Override
    public HealthStatus handle(final HealthCheckCommand command, final MemoryStorage storage) {
        return HealthStatus.HEALTHY;
    }
}
