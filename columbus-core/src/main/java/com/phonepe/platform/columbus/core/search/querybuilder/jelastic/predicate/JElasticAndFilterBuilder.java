package com.phonepe.platform.columbus.core.search.querybuilder.jelastic.predicate;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.grookage.jelastic.core.models.query.filter.Filter;
import com.phonepe.platform.columbus.core.search.querybuilder.jelastic.JElasticFilterBuilder;
import com.phonepe.platform.columbus.core.search.querybuilder.jelastic.JElasticFilterBuilderContext;
import com.phonepe.platform.columbus.core.utils.JElasticFilterUtils;
import com.phonepe.platform.columbus.models.core.search.filter.predicate.AndFilter;

@JsonTypeName(AndFilter.TYPE)
public class JElasticAndFilterBuilder implements JElasticFilterBuilder<AndFilter> {
    @Override
    public Filter visit(AndFilter filter, JElasticFilterBuilderContext context) {
        return JElasticFilterUtils.andFilter(filter.getFilters().stream()
            .map(context::visit)
            .toList());
    }
}
