package com.phonepe.commons.columbus.search.score;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Score function that simply multiplies the score by the weight value")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(WeightScore.TYPE)
public class WeightScore extends AbstractScoreFunction {

    public static final String TYPE = "WEIGHT";

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
