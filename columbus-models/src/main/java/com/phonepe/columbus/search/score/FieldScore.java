package com.phonepe.columbus.search.score;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Score function that uses a numeric field value to influence the score")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(FieldScore.TYPE)
public class FieldScore extends AbstractScoreFunction {

    public static final String TYPE = "FIELD";

    @JsonPropertyDescription("The numeric field whose value will be used in score calculation")
    private String field;

    @JsonPropertyDescription("Optional factor to multiply the field value by before using it in scoring")
    private Float factor;

    @JsonPropertyDescription("Default value to use if the field is missing from a document")
    private Float missing;

    @JsonPropertyDescription("Mathematical modifier to apply to the field value (none, log, log1p, log2p, ln, ln1p, ln2p, square, sqrt, reciprocal)")
    private String modifier;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
