package com.phonepe.platform.columbus.core.search.querybuilder.elastic.general;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.core.search.querybuilder.elastic.ElasticQueryBuilder;
import com.phonepe.platform.columbus.core.search.querybuilder.elastic.ElasticQueryBuilderContext;
import com.phonepe.platform.columbus.models.core.search.filter.general.NotEqualsFilter;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

@JsonTypeName(NotEqualsFilter.TYPE)
public class ElasticNotEqualsQueryBuilder<T> implements ElasticQueryBuilder<NotEqualsFilter<T>> {
    @Override
    public QueryBuilder visit(NotEqualsFilter<T> filter, ElasticQueryBuilderContext context) {
        return QueryBuilders.boolQuery()
            .mustNot(QueryBuilders.termsQuery(filter.getField(), context.extractValue(filter.getValue())));
    }
}
