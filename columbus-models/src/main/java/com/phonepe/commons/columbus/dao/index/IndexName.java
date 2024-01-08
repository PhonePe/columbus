package com.phonepe.commons.columbus.dao.index;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * An index identified by name.
 */
@Data
@Jacksonized
@SuperBuilder
@JsonTypeName(IndexName.TYPE)
public class IndexName implements Index {

    public static final String TYPE = "NAME";

    @NonNull
    private final String name;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
