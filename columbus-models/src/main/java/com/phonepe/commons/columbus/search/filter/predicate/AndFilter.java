package com.phonepe.commons.columbus.search.filter.predicate;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.filter.CompoundFilter;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Filter that matches documents where all child filters match (logical AND)")
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
