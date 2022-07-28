package com.phonepe.platform.columbus.core.visitors;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public abstract class Context<R, B, C extends Context<R, B, C>> {
    private final Resolver<R, B, C> resolver;

    public <T extends B> R visit(T value) {
        return resolver.resolve(value).visit(value, self());
    }

    protected abstract C self();
}
