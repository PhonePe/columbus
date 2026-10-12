package com.phonepe.columbus.internal.visitor.example.visitors.number;

import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public final class IntegerVisitor extends AbstractNumberVisitorContext<Integer, IntegerVisitor> {

    @Override
    protected @NonNull IntegerVisitor self() {
        return this;
    }
}
