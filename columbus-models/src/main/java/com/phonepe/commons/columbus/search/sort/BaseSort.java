package com.phonepe.commons.columbus.search.sort;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public abstract class BaseSort implements Sort {

    private SortOrder order;
}
