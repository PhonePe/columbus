package com.phonepe.platform.columbus.search.score;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(DecayScore.TYPE)
public class DecayScore extends BaseScoreFunction {

    public static final String TYPE = "DECAY";

    private String mode;

    private String field;

    private Object origin;
    private Object scale;
    private Object offset;

    private Double decay;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
