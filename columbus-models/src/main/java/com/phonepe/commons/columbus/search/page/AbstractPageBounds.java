package com.phonepe.commons.columbus.search.page;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public abstract class AbstractPageBounds implements PageBounds {

    private Integer size;
}
