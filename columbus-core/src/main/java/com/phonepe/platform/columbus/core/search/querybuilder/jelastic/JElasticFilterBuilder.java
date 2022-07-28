package com.phonepe.platform.columbus.core.search.querybuilder.jelastic;

import com.phonepe.platform.columbus.core.search.querybuilder.BaseQueryBuilder;
import com.phonepe.platform.columbus.models.core.search.filter.Filter;

public interface JElasticFilterBuilder<T extends Filter>
    extends BaseQueryBuilder<com.grookage.jelastic.core.models.query.filter.Filter, T, JElasticFilterBuilderContext> {
}
