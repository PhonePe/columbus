package com.phonepe.platform.columbus.storage.memory.handlers;

import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.storage.commands.VersionCommand;
import com.phonepe.platform.columbus.storage.memory.MemoryStorage;

@RegistryKey(type = VersionCommand.class)
public class VersionHandler implements MemoryStorageCommandHandler<String, VersionCommand> {

    @Override
    public String handle(final VersionCommand command, final MemoryStorage storage) {
        return MemoryStorage.VERSION;
    }
}
