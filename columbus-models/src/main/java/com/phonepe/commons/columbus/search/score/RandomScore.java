package com.phonepe.commons.columbus.search.score;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Score function that generates random scores for documents, useful for random ordering")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(RandomScore.TYPE)
public class RandomScore extends AbstractScoreFunction {

    public static final String TYPE = "RANDOM";

    @JsonPropertyDescription("Field to use for consistent random scoring per unique field value")
    private String field;

    @JsonPropertyDescription("Optional seed for reproducible random score generation")
    private Integer seed;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
