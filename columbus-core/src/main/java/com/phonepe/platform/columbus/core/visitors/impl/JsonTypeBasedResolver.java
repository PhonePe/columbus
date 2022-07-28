package com.phonepe.platform.columbus.core.visitors.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.platform.columbus.core.visitors.Context;
import com.phonepe.platform.columbus.core.visitors.Resolver;
import com.phonepe.platform.columbus.core.visitors.Visitor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class JsonTypeBasedResolver<R, B, C extends Context<R, B, C>, V extends Visitor<R, ? extends B, B, C>>
    implements Resolver<R, B, C> {
    private final ObjectMapper mapper;
    private final TypeReference<V> baseVisitor;

    @Override
    @SuppressWarnings("unchecked")
    public <T extends B> Visitor<R, T, B, C> resolve(T value) {
        return (Visitor<R, T, B, C>) mapper.convertValue(value, baseVisitor);
    }
}
