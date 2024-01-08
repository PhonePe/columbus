package com.phonepe.commons.columbus.search.filter.predicate;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.filter.CompoundFilter;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(OrFilter.TYPE)
public class OrFilter extends CompoundFilter {

    public static final String TYPE = "OR";

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
