package com.phonepe.platform.columbus.core.search.querybuilder.elastic.range;

import com.phonepe.platform.columbus.core.search.querybuilder.elastic.ElasticQueryBuilder;
import com.phonepe.platform.columbus.core.search.querybuilder.elastic.ElasticQueryBuilderContext;
import com.phonepe.platform.columbus.models.core.search.filter.range.RangeFilter;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;

public abstract class ElasticRangeQueryBuilder<T> implements ElasticQueryBuilder<RangeFilter<T>> {
    @Override
    public QueryBuilder visit(RangeFilter<T> filter, ElasticQueryBuilderContext context) {
        return rangeQuery(QueryBuilders.rangeQuery(filter.getField()), context.extractValue(filter.getValue()));
    }

    protected abstract QueryBuilder rangeQuery(RangeQueryBuilder queryBuilder, T value);
}
