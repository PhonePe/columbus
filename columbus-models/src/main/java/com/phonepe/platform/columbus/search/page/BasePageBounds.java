package com.phonepe.platform.columbus.search.page;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public abstract class BasePageBounds implements PageBounds {

    private Integer size;
}
