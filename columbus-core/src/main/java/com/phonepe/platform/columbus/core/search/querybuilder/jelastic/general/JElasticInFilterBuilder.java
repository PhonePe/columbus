package com.phonepe.platform.columbus.core.search.querybuilder.jelastic.general;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.grookage.jelastic.core.models.query.filter.Filter;
import com.phonepe.platform.columbus.core.search.querybuilder.jelastic.JElasticFilterBuilder;
import com.phonepe.platform.columbus.core.search.querybuilder.jelastic.JElasticFilterBuilderContext;
import com.phonepe.platform.columbus.core.utils.JElasticFilterUtils;
import com.phonepe.platform.columbus.models.core.search.filter.general.InFilter;

@JsonTypeName(InFilter.TYPE)
public class JElasticInFilterBuilder<T> implements JElasticFilterBuilder<InFilter<T>> {
    @Override
    public Filter visit(InFilter<T> filter, JElasticFilterBuilderContext context) {
        return JElasticFilterUtils.inFilter(filter.getField(), context.extractValue(filter.getValues()));
    }
}
