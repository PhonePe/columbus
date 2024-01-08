package com.phonepe.commons.columbus.test.storage.memory.handlers;

import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.storage.commands.HealthCheckCommand;
import com.phonepe.commons.columbus.storage.commands.HealthCheckCommand.HealthStatus;
import com.phonepe.commons.columbus.test.storage.memory.MemoryStorage;

@RegistryKey(type = HealthCheckCommand.class)
public class HealthCheckHandler implements MemoryStorageCommandHandler<HealthStatus, HealthCheckCommand> {

    @Override
    public HealthStatus handle(final HealthCheckCommand command, final MemoryStorage storage) {
        return HealthStatus.HEALTHY;
    }
}
