package com.phonepe.platform.columbus.dropwizard.storage.memory;

import com.phonepe.platform.columbus.storage.BaseStorageClient;
import io.dropwizard.setup.Environment;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
public class MemoryStorage extends BaseStorageClient<MemoryStorageConfiguration, MemoryStorage> {

    public static final String VERSION = "1.0.0";

    @NonNull
    private final Environment environment;

    @Override
    protected @NonNull MemoryStorage self() {
        return this;
    }
}
