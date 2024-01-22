package com.phonepe.commons.columbus.search.sort;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public abstract class AbstractSort implements Sort {

    private SortOrder order;
}
