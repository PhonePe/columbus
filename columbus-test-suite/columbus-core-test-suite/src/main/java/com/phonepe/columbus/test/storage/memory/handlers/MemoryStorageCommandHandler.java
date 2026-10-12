package com.phonepe.columbus.test.storage.memory.handlers;

import com.phonepe.columbus.command.Command;
import com.phonepe.columbus.storage.StorageCommandHandler;
import com.phonepe.columbus.test.storage.memory.MemoryStorage;

public interface MemoryStorageCommandHandler<R, C extends Command<R>>
        extends StorageCommandHandler<R, C, MemoryStorage> {

    @Override
    R handle(C command, MemoryStorage storage);
}
