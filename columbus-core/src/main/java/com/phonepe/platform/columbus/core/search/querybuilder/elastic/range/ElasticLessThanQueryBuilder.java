package com.phonepe.platform.columbus.core.search.querybuilder.elastic.range;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.models.core.search.filter.range.LessThanFilter;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;

@JsonTypeName(LessThanFilter.TYPE)
public class ElasticLessThanQueryBuilder<T> extends ElasticRangeQueryBuilder<T> {
    @Override
    protected QueryBuilder rangeQuery(RangeQueryBuilder queryBuilder, T value) {
        return queryBuilder.lt(value);
    }
}
