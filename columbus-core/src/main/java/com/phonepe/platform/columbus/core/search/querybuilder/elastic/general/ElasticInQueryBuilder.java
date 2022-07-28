package com.phonepe.platform.columbus.core.search.querybuilder.elastic.general;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.core.search.querybuilder.elastic.ElasticQueryBuilder;
import com.phonepe.platform.columbus.core.search.querybuilder.elastic.ElasticQueryBuilderContext;
import com.phonepe.platform.columbus.models.core.search.filter.general.InFilter;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

@JsonTypeName(InFilter.TYPE)
public class ElasticInQueryBuilder<T> implements ElasticQueryBuilder<InFilter<T>> {
    @Override
    public QueryBuilder visit(InFilter<T> filter, ElasticQueryBuilderContext context) {
        return QueryBuilders.termsQuery(filter.getField(), context.extractValue(filter.getValues()));
    }
}
