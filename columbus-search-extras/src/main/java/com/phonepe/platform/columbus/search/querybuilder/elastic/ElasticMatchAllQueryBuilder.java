package com.phonepe.platform.columbus.search.querybuilder.elastic;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.core.search.querybuilder.elastic.ElasticQueryBuilder;
import com.phonepe.platform.columbus.core.search.querybuilder.elastic.ElasticQueryBuilderContext;
import com.phonepe.platform.columbus.models.search.filter.MatchAllFilter;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

@JsonTypeName(MatchAllFilter.TYPE)
public class ElasticMatchAllQueryBuilder implements ElasticQueryBuilder<MatchAllFilter> {
    @Override
    public QueryBuilder visit(MatchAllFilter filter, ElasticQueryBuilderContext context) {
        return QueryBuilders.matchAllQuery();
    }
}
