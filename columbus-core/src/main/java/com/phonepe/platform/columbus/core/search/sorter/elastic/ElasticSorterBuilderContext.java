package com.phonepe.platform.columbus.core.search.sorter.elastic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.platform.columbus.core.search.sorter.SorterBuilderBaseContext;
import com.phonepe.platform.columbus.core.visitors.impl.JsonTypeBasedResolver;
import com.phonepe.platform.columbus.models.core.search.sorter.Sorter;
import lombok.experimental.SuperBuilder;
import org.elasticsearch.search.sort.SortBuilder;

@SuperBuilder
public class ElasticSorterBuilderContext
    extends SorterBuilderBaseContext<SortBuilder<?>, ElasticSorterBuilderContext> {
    @Override
    protected ElasticSorterBuilderContext self() {
        return this;
    }

    public static ElasticSorterBuilderContextBuilder<?, ?> withDefaults(ObjectMapper mapper) {
        return builder().resolver(
            JsonTypeBasedResolver.<SortBuilder<?>, Sorter, ElasticSorterBuilderContext,
                    ElasticSorterBuilder<?>>builder()
                .mapper(mapper)
                .baseVisitor(new TypeReference<>() {
                })
                .build());
    }
}
