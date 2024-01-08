package com.phonepe.commons.columbus.test.storage.memory.handlers;

import com.phonepe.commons.columbus.command.Command;
import com.phonepe.commons.columbus.storage.StorageCommandHandler;
import com.phonepe.commons.columbus.test.storage.memory.MemoryStorage;

public interface MemoryStorageCommandHandler<R, C extends Command<R>>
        extends StorageCommandHandler<R, C, MemoryStorage> {

    @Override
    R handle(C command, MemoryStorage storage);
}
