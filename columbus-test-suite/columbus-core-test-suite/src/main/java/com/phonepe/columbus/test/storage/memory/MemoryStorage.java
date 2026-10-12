package com.phonepe.columbus.test.storage.memory;

import com.phonepe.columbus.storage.AbstractStorageClient;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class MemoryStorage extends AbstractStorageClient<MemoryStorageConfiguration, MemoryStorage> {

    public static final String VERSION = "1.0.0";

    @Override
    protected @NonNull MemoryStorage self() {
        return this;
    }
}
