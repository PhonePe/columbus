package com.phonepe.platform.columbus.core.search.querybuilder.elastic.predicate;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.core.search.querybuilder.elastic.ElasticQueryBuilder;
import com.phonepe.platform.columbus.core.search.querybuilder.elastic.ElasticQueryBuilderContext;
import com.phonepe.platform.columbus.models.core.search.filter.predicate.OrFilter;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

@JsonTypeName(OrFilter.TYPE)
public class ElasticOrQueryBuilder implements ElasticQueryBuilder<OrFilter> {
    @Override
    public QueryBuilder visit(OrFilter filter, ElasticQueryBuilderContext context) {
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        filter.getFilters().forEach(f -> query.should(context.visit(f)));
        return query;
    }
}
