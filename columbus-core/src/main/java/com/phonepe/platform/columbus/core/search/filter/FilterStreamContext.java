package com.phonepe.platform.columbus.core.search.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.platform.columbus.core.visitors.impl.ElementStreamContext;
import com.phonepe.platform.columbus.core.visitors.impl.JsonTypeBasedResolver;
import com.phonepe.platform.columbus.models.core.search.filter.Filter;
import lombok.experimental.SuperBuilder;

import java.util.stream.Stream;

@SuperBuilder
public class FilterStreamContext extends ElementStreamContext<Filter, FilterStreamContext> {
    @Override
    protected FilterStreamContext self() {
        return this;
    }

    public static FilterStreamContext withDefaults(ObjectMapper mapper) {
        return builder().resolver(
            JsonTypeBasedResolver.<Stream<Filter>, Filter, FilterStreamContext, FilterStreamVisitor<?>>builder()
                .mapper(mapper)
                .baseVisitor(new TypeReference<>() {
                })
                .build()).build();
    }
}
