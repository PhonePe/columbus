package com.phonepe.commons.columbus.test.storage.memory;

import com.phonepe.commons.columbus.storage.BaseStorageClient;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class MemoryStorage extends BaseStorageClient<MemoryStorageConfiguration, MemoryStorage> {

    public static final String VERSION = "1.0.0";

    @Override
    protected @NonNull MemoryStorage self() {
        return this;
    }
}
