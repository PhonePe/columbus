package com.phonepe.platform.columbus.search.filter.score;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.search.filter.Filter;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(FixedScoreFilter.TYPE)
public class FixedScoreFilter implements Filter {

    public static final String TYPE = "FIXED_SCORE";

    private Filter filter;

    private Float boost;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
