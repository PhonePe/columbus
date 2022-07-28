package com.phonepe.platform.columbus.core.search.querybuilder.elastic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.platform.columbus.core.search.querybuilder.QueryBuilderBaseContext;
import com.phonepe.platform.columbus.core.visitors.impl.JsonTypeBasedResolver;
import com.phonepe.platform.columbus.models.core.search.filter.Filter;
import lombok.experimental.SuperBuilder;
import org.elasticsearch.index.query.QueryBuilder;

@SuperBuilder
public class ElasticQueryBuilderContext extends QueryBuilderBaseContext<QueryBuilder, ElasticQueryBuilderContext> {
    @Override
    protected ElasticQueryBuilderContext self() {
        return this;
    }

    public static ElasticQueryBuilderContextBuilder<?, ?> withDefaults(ObjectMapper mapper) {
        return builder().resolver(
            JsonTypeBasedResolver.<QueryBuilder, Filter, ElasticQueryBuilderContext, ElasticQueryBuilder<?>>builder()
                .mapper(mapper)
                .baseVisitor(new TypeReference<>() {
                })
                .build());
    }
}
