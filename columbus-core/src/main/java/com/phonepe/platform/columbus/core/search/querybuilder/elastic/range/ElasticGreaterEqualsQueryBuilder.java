package com.phonepe.platform.columbus.core.search.querybuilder.elastic.range;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.models.core.search.filter.range.GreaterEqualsFilter;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;

@JsonTypeName(GreaterEqualsFilter.TYPE)
public class ElasticGreaterEqualsQueryBuilder<T> extends ElasticRangeQueryBuilder<T> {
    @Override
    protected QueryBuilder rangeQuery(RangeQueryBuilder queryBuilder, T value) {
        return queryBuilder.gte(value);
    }
}
