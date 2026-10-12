package com.phonepe.columbus.search.sort;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Sort that orders documents based on the value of a specific field")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(FieldSort.TYPE)
public class FieldSort extends AbstractSort {

    public static final String TYPE = "FIELD";

    @JsonPropertyDescription("The field name to sort by")
    private String field;

    @JsonPropertyDescription("How to pick the sort value when a document has multiple values (min, max, sum, avg, median)")
    private String sortMode;

    @JsonPropertyDescription("Numeric type for numeric fields (long, double, date, date_nanos)")
    private String numericType;

    @JsonPropertyDescription("Fallback field type when the field is unmapped")
    private String fallbackFieldType;

    @JsonPropertyDescription("Value to use for documents where the field is missing")
    private Object missingValue;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
