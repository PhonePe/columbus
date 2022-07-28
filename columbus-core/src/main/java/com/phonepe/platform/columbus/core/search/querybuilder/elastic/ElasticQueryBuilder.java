package com.phonepe.platform.columbus.core.search.querybuilder.elastic;

import com.phonepe.platform.columbus.core.search.querybuilder.BaseQueryBuilder;
import com.phonepe.platform.columbus.models.core.search.filter.Filter;
import org.elasticsearch.index.query.QueryBuilder;

public interface ElasticQueryBuilder<T extends Filter>
    extends BaseQueryBuilder<QueryBuilder, T, ElasticQueryBuilderContext> {
}
