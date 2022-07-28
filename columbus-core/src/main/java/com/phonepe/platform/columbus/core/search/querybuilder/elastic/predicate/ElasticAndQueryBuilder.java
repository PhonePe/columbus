package com.phonepe.platform.columbus.core.search.querybuilder.elastic.predicate;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.core.search.querybuilder.elastic.ElasticQueryBuilder;
import com.phonepe.platform.columbus.core.search.querybuilder.elastic.ElasticQueryBuilderContext;
import com.phonepe.platform.columbus.models.core.search.filter.predicate.AndFilter;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

@JsonTypeName(AndFilter.TYPE)
public class ElasticAndQueryBuilder implements ElasticQueryBuilder<AndFilter> {
    @Override
    public QueryBuilder visit(AndFilter filter, ElasticQueryBuilderContext context) {
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        filter.getFilters().forEach(f -> query.must(context.visit(f)));
        return query;
    }
}
