package com.phonepe.platform.columbus.dropwizard.storage.memory.handlers;

import com.phonepe.platform.columbus.command.Command;
import com.phonepe.platform.columbus.dropwizard.storage.memory.MemoryStorage;
import com.phonepe.platform.columbus.storage.StorageCommandHandler;

public interface MemoryStorageCommandHandler<R, C extends Command<R>>
        extends StorageCommandHandler<R, C, MemoryStorage> {

    @Override
    R handle(C command, MemoryStorage storage);
}
