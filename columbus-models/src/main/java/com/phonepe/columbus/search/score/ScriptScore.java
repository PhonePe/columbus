package com.phonepe.columbus.search.score;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.columbus.search.misc.script.Script;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Score function that computes scores using a custom script for maximum flexibility")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(ScriptScore.TYPE)
public class ScriptScore extends AbstractScoreFunction {

    public static final String TYPE = "SCRIPT";

    @JsonPropertyDescription("Script that computes the score value for each document")
    private Script script;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
