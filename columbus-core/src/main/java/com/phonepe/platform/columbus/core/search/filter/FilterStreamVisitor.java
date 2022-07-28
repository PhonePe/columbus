package com.phonepe.platform.columbus.core.search.filter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.phonepe.platform.columbus.core.visitors.impl.ElementStreamVisitor;
import com.phonepe.platform.columbus.models.core.search.filter.Filter;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "filterType"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class FilterStreamVisitor<T extends Filter>
    extends ElementStreamVisitor<T, Filter, FilterStreamContext> {
}
