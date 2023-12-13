package com.phonepe.platform.columbus.internal.visitor.example.visitors.number;

import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public final class NumberVisitor extends NumberVisitorContext<NumberVisitor> {

    @Override
    protected @NonNull NumberVisitor self() {
        return this;
    }
}
