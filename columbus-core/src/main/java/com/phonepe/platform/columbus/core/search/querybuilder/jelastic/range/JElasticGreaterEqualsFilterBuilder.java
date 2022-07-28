package com.phonepe.platform.columbus.core.search.querybuilder.jelastic.range;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.grookage.jelastic.core.models.query.filter.Filter;
import com.phonepe.platform.columbus.core.utils.JElasticFilterUtils;
import com.phonepe.platform.columbus.models.core.search.filter.range.GreaterEqualsFilter;

@JsonTypeName(GreaterEqualsFilter.TYPE)
public class JElasticGreaterEqualsFilterBuilder<T> extends JElasticRangeFilterBuilder<T> {
    @Override
    protected Filter rangeFilter(String field, T value) {
        return JElasticFilterUtils.greaterEquals(field, value);
    }
}
