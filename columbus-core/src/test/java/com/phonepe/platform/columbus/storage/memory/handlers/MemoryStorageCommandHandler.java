package com.phonepe.platform.columbus.storage.memory.handlers;

import com.phonepe.platform.columbus.command.Command;
import com.phonepe.platform.columbus.storage.StorageCommandHandler;
import com.phonepe.platform.columbus.storage.memory.MemoryStorage;

public interface MemoryStorageCommandHandler<R, C extends Command<R>>
        extends StorageCommandHandler<R, C, MemoryStorage> {

    @Override
    R handle(C command, MemoryStorage storage);
}
