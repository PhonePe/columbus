package com.phonepe.columbus.dao.id;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * An id that holds its actual string representation.
 */
@Data
@Jacksonized
@SuperBuilder
@JsonTypeName(ValueId.TYPE)
public class ValueId implements Id {

    public static final String TYPE = "VALUE";

    @NonNull
    private final String value;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
