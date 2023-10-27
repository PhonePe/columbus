package com.phonepe.platform.columbus.search.sort;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(ScoreSort.TYPE)
public class ScoreSort extends BaseSort {

    public static final String TYPE = "SCORE";

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
