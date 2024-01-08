package com.phonepe.commons.columbus.search.score;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(FieldScore.TYPE)
public class FieldScore extends BaseScoreFunction {

    public static final String TYPE = "FIELD";

    private String field;

    private Float factor;

    private Float missing;

    private String modifier;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
