package com.phonepe.platform.columbus.core.search.querybuilder.jelastic.range;

import com.grookage.jelastic.core.models.query.filter.Filter;
import com.phonepe.platform.columbus.core.search.querybuilder.jelastic.JElasticFilterBuilder;
import com.phonepe.platform.columbus.core.search.querybuilder.jelastic.JElasticFilterBuilderContext;
import com.phonepe.platform.columbus.models.core.search.filter.range.RangeFilter;

public abstract class JElasticRangeFilterBuilder<T> implements JElasticFilterBuilder<RangeFilter<T>> {
    @Override
    public Filter visit(RangeFilter<T> filter, JElasticFilterBuilderContext context) {
        return rangeFilter(filter.getField(), context.extractValue(filter.getValue()));
    }

    protected abstract Filter rangeFilter(String field, T value);
}
