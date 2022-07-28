package com.phonepe.platform.columbus.search.querybuilder.elastic.string;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.core.search.querybuilder.elastic.ElasticQueryBuilder;
import com.phonepe.platform.columbus.core.search.querybuilder.elastic.ElasticQueryBuilderContext;
import com.phonepe.platform.columbus.models.search.filter.string.PrefixMatchFilter;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

@JsonTypeName(PrefixMatchFilter.TYPE)
public class ElasticPrefixMatchQueryBuilder implements ElasticQueryBuilder<PrefixMatchFilter> {
    @Override
    public QueryBuilder visit(PrefixMatchFilter filter, ElasticQueryBuilderContext context) {
        return QueryBuilders.prefixQuery(filter.getField(), context.extractValue(filter.getValue()));
    }
}
