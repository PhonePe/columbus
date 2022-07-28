package com.phonepe.platform.columbus.search.querybuilder.elastic.string;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.core.search.querybuilder.elastic.ElasticQueryBuilder;
import com.phonepe.platform.columbus.core.search.querybuilder.elastic.ElasticQueryBuilderContext;
import com.phonepe.platform.columbus.models.search.filter.string.PhrasePrefixMatchFilter;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

@JsonTypeName(PhrasePrefixMatchFilter.TYPE)
public class ElasticMatchPhrasePrefixQueryBuilder implements ElasticQueryBuilder<PhrasePrefixMatchFilter> {
    @Override
    public QueryBuilder visit(PhrasePrefixMatchFilter filter, ElasticQueryBuilderContext context) {
        return QueryBuilders.matchPhrasePrefixQuery(filter.getField(), context.extractValue(filter.getValue()));
    }
}
