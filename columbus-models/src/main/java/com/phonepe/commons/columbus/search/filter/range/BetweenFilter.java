package com.phonepe.commons.columbus.search.filter.range;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.filter.FieldFilter;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(BetweenFilter.TYPE)
public class BetweenFilter extends FieldFilter {

    public static final String TYPE = "BETWEEN";

    private Object from;
    private Object to;

    @Builder.Default
    @Accessors(fluent = true)
    private boolean includeLower = true;

    @Builder.Default
    @Accessors(fluent = true)
    private boolean includeUpper = true;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
