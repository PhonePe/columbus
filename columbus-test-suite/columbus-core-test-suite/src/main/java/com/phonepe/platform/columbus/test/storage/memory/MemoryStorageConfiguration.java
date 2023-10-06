package com.phonepe.platform.columbus.test.storage.memory;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.storage.StorageConfiguration;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder
@JsonTypeName(MemoryStorageConfiguration.TYPE)
public class MemoryStorageConfiguration extends StorageConfiguration {

    public static final String TYPE = "MEMORY";

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
