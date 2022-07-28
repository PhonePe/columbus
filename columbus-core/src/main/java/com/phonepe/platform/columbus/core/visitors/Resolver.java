package com.phonepe.platform.columbus.core.visitors;

public interface Resolver<R, B, C extends Context<R, B, C>> {
    <T extends B> Visitor<R, T, B, C> resolve(T value);
}
