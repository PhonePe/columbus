package com.phonepe.platform.columbus.dao.index;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * A key to a configured index.
 */
@Data
@Jacksonized
@SuperBuilder
@JsonTypeName(IndexReference.TYPE)
public class IndexReference implements Index {

    public static final String TYPE = "REFERENCE";

    @NonNull
    private final String key;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
