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
