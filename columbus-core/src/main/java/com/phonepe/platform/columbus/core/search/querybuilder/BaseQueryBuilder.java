package com.phonepe.platform.columbus.core.search.querybuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.phonepe.platform.columbus.core.visitors.Visitor;
import com.phonepe.platform.columbus.models.core.search.filter.Filter;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "filterType"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface BaseQueryBuilder<R, T extends Filter, C extends QueryBuilderBaseContext<R, C>>
    extends Visitor<R, T, Filter, C> {
}
