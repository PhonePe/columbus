package com.phonepe.columbus.search.sort;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.columbus.search.misc.script.Script;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Sort that orders documents based on a custom script that computes sort values dynamically")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(ScriptSort.TYPE)
public class ScriptSort extends AbstractSort {

    public static final String TYPE = "SCRIPT";

    @JsonPropertyDescription("The script that computes the sort value for each document")
    private Script script;

    @JsonPropertyDescription("The data type of values returned by the script (string, number)")
    private String valueType;

    @JsonPropertyDescription("How to pick the sort value when the script returns multiple values (min, max, sum, avg)")
    private String sortMode;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
