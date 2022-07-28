package com.phonepe.platform.columbus.core.search.querybuilder.jelastic.range;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.grookage.jelastic.core.models.query.filter.Filter;
import com.phonepe.platform.columbus.core.search.querybuilder.jelastic.JElasticFilterBuilder;
import com.phonepe.platform.columbus.core.search.querybuilder.jelastic.JElasticFilterBuilderContext;
import com.phonepe.platform.columbus.core.utils.JElasticFilterUtils;
import com.phonepe.platform.columbus.models.core.search.filter.range.BetweenFilter;

@JsonTypeName(BetweenFilter.TYPE)
public class JElasticBetweenFilterBuilder<T> implements JElasticFilterBuilder<BetweenFilter<T>> {
    @Override
    public Filter visit(BetweenFilter<T> filter, JElasticFilterBuilderContext context) {
        return JElasticFilterUtils.between(filter.getField(), context.extractValue(filter.getFrom()), context.extractValue(filter.getTo()));
    }
}
