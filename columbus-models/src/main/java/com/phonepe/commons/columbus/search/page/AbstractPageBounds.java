package com.phonepe.commons.columbus.search.page;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@JsonClassDescription("Abstract base class for all page bounds implementations providing the common size property")
@Data
@SuperBuilder(toBuilder = true)
public abstract class AbstractPageBounds implements PageBounds {

    @JsonPropertyDescription("Maximum number of results to return in this page")
    private Integer size;
}
