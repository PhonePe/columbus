package com.phonepe.commons.columbus.search.sort;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@JsonClassDescription("Abstract base class for all sort implementations providing the common order property")
@Data
@SuperBuilder(toBuilder = true)
public abstract class AbstractSort implements Sort {

    @JsonPropertyDescription("Sort order direction (ASC for ascending, DESC for descending)")
    private SortOrder order;
}
