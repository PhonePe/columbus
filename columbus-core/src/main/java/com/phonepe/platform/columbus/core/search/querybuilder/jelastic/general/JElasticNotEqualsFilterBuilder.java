package com.phonepe.platform.columbus.core.search.querybuilder.jelastic.general;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.grookage.jelastic.core.models.query.filter.Filter;
import com.phonepe.platform.columbus.core.search.querybuilder.jelastic.JElasticFilterBuilder;
import com.phonepe.platform.columbus.core.search.querybuilder.jelastic.JElasticFilterBuilderContext;
import com.phonepe.platform.columbus.core.utils.JElasticFilterUtils;
import com.phonepe.platform.columbus.models.core.search.filter.general.NotEqualsFilter;

@JsonTypeName(NotEqualsFilter.TYPE)
public class JElasticNotEqualsFilterBuilder<T> implements JElasticFilterBuilder<NotEqualsFilter<T>> {
    @Override
    public Filter visit(NotEqualsFilter<T> filter, JElasticFilterBuilderContext context) {
        return JElasticFilterUtils.notEqualsFilter(filter.getField(), context.extractValue(filter.getValue()));
    }
}
