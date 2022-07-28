package com.phonepe.platform.columbus.core.search.querybuilder.jelastic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.platform.columbus.core.search.querybuilder.QueryBuilderBaseContext;
import com.phonepe.platform.columbus.core.visitors.impl.JsonTypeBasedResolver;
import com.phonepe.platform.columbus.models.core.search.filter.Filter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class JElasticFilterBuilderContext
    extends QueryBuilderBaseContext<com.grookage.jelastic.core.models.query.filter.Filter, JElasticFilterBuilderContext> {
    @Override
    protected JElasticFilterBuilderContext self() {
        return this;
    }

    public static JElasticFilterBuilderContextBuilder<?, ?> withDefaults(ObjectMapper mapper) {
        return builder().resolver(
            JsonTypeBasedResolver.<com.grookage.jelastic.core.models.query.filter.Filter, Filter, JElasticFilterBuilderContext, JElasticFilterBuilder<?>>builder()
                .mapper(mapper)
                .baseVisitor(new TypeReference<>() {
                })
                .build());
    }
}
