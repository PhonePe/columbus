package com.phonepe.platform.columbus.dropwizard.storage.memory.handlers;

import com.phonepe.platform.columbus.dropwizard.storage.memory.MemoryStorage;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.storage.commands.VersionCommand;

@RegistryKey(type = VersionCommand.class)
public class VersionHandler implements MemoryStorageCommandHandler<String, VersionCommand> {

    @Override
    public String handle(final VersionCommand command, final MemoryStorage storage) {
        return MemoryStorage.VERSION;
    }
}
