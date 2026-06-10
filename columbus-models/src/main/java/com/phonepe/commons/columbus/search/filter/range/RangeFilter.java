package com.phonepe.commons.columbus.search.filter.range;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.phonepe.commons.columbus.search.filter.FieldFilter;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@JsonClassDescription("Abstract base class for range comparison filters")
@Data
@SuperBuilder(toBuilder = true)
public abstract class RangeFilter extends FieldFilter {

    @JsonPropertyDescription("The value to compare against")
    private Object value;
}
