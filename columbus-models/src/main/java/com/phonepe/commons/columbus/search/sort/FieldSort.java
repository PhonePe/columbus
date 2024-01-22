package com.phonepe.commons.columbus.search.sort;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(FieldSort.TYPE)
public class FieldSort extends AbstractSort {

    public static final String TYPE = "FIELD";

    private String field;

    private String sortMode;

    private String numericType;
    private String fallbackFieldType;
    private Object missingValue;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
