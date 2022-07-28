package com.phonepe.platform.columbus.core.search.sorter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.phonepe.platform.columbus.core.visitors.Visitor;
import com.phonepe.platform.columbus.models.core.search.sorter.Sorter;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "sorterType"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface BaseSorterBuilder<R, T extends Sorter, C extends SorterBuilderBaseContext<R, C>>
    extends Visitor<R, T, Sorter, C> {
}
