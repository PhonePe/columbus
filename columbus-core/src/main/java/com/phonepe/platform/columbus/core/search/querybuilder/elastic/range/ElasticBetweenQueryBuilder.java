package com.phonepe.platform.columbus.core.search.querybuilder.elastic.range;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.core.search.querybuilder.elastic.ElasticQueryBuilder;
import com.phonepe.platform.columbus.core.search.querybuilder.elastic.ElasticQueryBuilderContext;
import com.phonepe.platform.columbus.models.core.search.filter.range.BetweenFilter;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

@JsonTypeName(BetweenFilter.TYPE)
public class ElasticBetweenQueryBuilder<T> implements ElasticQueryBuilder<BetweenFilter<T>> {
    @Override
    public QueryBuilder visit(BetweenFilter<T> filter, ElasticQueryBuilderContext context) {
        return QueryBuilders.rangeQuery(filter.getField())
            .from(context.extractValue(filter.getFrom()), filter.isIncludeLower())
            .to(context.extractValue(filter.getTo()), filter.isIncludeUpper());
    }
}
