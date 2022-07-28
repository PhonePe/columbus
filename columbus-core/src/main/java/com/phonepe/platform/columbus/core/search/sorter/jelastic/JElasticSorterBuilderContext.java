package com.phonepe.platform.columbus.core.search.sorter.jelastic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.platform.columbus.core.search.sorter.SorterBuilderBaseContext;
import com.phonepe.platform.columbus.core.visitors.impl.JsonTypeBasedResolver;
import com.phonepe.platform.columbus.models.core.search.sorter.Sorter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class JElasticSorterBuilderContext
    extends SorterBuilderBaseContext<com.grookage.jelastic.core.models.query.sorter.Sorter, JElasticSorterBuilderContext> {
    @Override
    protected JElasticSorterBuilderContext self() {
        return this;
    }

    public static JElasticSorterBuilderContextBuilder<?, ?> withDefaults(ObjectMapper mapper) {
        return builder().resolver(
            JsonTypeBasedResolver.<com.grookage.jelastic.core.models.query.sorter.Sorter, Sorter, JElasticSorterBuilderContext, JElasticSorterBuilder<?>>builder()
                .mapper(mapper)
                .baseVisitor(new TypeReference<>() {
                })
                .build());
    }
}
