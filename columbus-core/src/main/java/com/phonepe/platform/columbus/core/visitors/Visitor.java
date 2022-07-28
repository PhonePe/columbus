package com.phonepe.platform.columbus.core.visitors;

public interface Visitor<R, T extends B, B, C extends Context<R, B, C>> {
    R visit(T value, C context);
}
