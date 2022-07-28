package com.phonepe.platform.columbus.core.search.sorter;

import com.phonepe.platform.columbus.core.visitors.Context;
import com.phonepe.platform.columbus.models.core.search.sorter.Sorter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public abstract class SorterBuilderBaseContext<R, C extends SorterBuilderBaseContext<R, C>>
    extends Context<R, Sorter, C> {
}
