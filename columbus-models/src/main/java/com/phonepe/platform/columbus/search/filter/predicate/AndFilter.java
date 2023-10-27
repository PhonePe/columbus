package com.phonepe.platform.columbus.search.filter.predicate;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.search.filter.CompoundFilter;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(AndFilter.TYPE)
public class AndFilter extends CompoundFilter {

    public static final String TYPE = "AND";

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
