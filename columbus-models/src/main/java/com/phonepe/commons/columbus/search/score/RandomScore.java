package com.phonepe.commons.columbus.search.score;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(RandomScore.TYPE)
public class RandomScore extends AbstractScoreFunction {

    public static final String TYPE = "RANDOM";

    private String field;

    private Integer seed;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
