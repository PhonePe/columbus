package com.phonepe.platform.columbus.core.search.querybuilder.elastic.general;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.core.search.querybuilder.elastic.ElasticQueryBuilder;
import com.phonepe.platform.columbus.core.search.querybuilder.elastic.ElasticQueryBuilderContext;
import com.phonepe.platform.columbus.models.core.search.filter.general.EqualsFilter;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

@JsonTypeName(EqualsFilter.TYPE)
public class ElasticEqualsQueryBuilder<T> implements ElasticQueryBuilder<EqualsFilter<T>> {
    @Override
    public QueryBuilder visit(EqualsFilter<T> filter, ElasticQueryBuilderContext context) {
        return QueryBuilders.termsQuery(filter.getField(), context.extractValue(filter.getValue()));
    }
}
