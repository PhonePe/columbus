package com.phonepe.platform.columbus.core.search.querybuilder.elastic.string;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.core.search.querybuilder.elastic.ElasticQueryBuilder;
import com.phonepe.platform.columbus.core.search.querybuilder.elastic.ElasticQueryBuilderContext;
import com.phonepe.platform.columbus.models.core.search.filter.general.WildCardFilter;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;


@JsonTypeName(WildCardFilter.TYPE)
public class ElasticWildCardQueryBuilder implements ElasticQueryBuilder<WildCardFilter> {

    @Override
    public QueryBuilder visit(WildCardFilter filter, ElasticQueryBuilderContext context) {
        return QueryBuilders.wildcardQuery(filter.getField(),
            String.valueOf(context.extractValue(filter.getValue())));
    }
}
