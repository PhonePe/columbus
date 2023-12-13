package com.phonepe.platform.columbus.internal.visitor.example.visitors.number;

import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public final class FloatVisitor extends AbstractNumberVisitorContext<Float, FloatVisitor> {

    @Override
    protected @NonNull FloatVisitor self() {
        return this;
    }
}
