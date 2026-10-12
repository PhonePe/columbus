package com.phonepe.columbus.test.storage.memory.handlers;

import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.commands.VersionCommand;
import com.phonepe.columbus.test.storage.memory.MemoryStorage;

@RegistryKey(type = VersionCommand.class)
public class VersionHandler implements MemoryStorageCommandHandler<String, VersionCommand> {

    @Override
    public String handle(final VersionCommand command, final MemoryStorage storage) {
        return MemoryStorage.VERSION;
    }
}
