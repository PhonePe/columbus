package com.phonepe.platform.columbus.core.search.querybuilder.elastic.range;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.models.core.search.filter.range.LessEqualsFilter;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;

@JsonTypeName(LessEqualsFilter.TYPE)
public class ElasticLessEqualsQueryBuilder<T> extends ElasticRangeQueryBuilder<T> {
    @Override
    protected QueryBuilder rangeQuery(RangeQueryBuilder queryBuilder, T value) {
        return queryBuilder.lte(value);
    }
}
