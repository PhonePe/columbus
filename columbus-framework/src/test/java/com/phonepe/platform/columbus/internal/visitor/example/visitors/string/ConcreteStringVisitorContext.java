package com.phonepe.platform.columbus.internal.visitor.example.visitors.string;

import com.phonepe.platform.columbus.internal.visitor.example.visitors.ModelVisitorContext;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public final class ConcreteStringVisitorContext extends ModelVisitorContext<String, ConcreteStringVisitorContext> {

    @Override
    protected @NonNull ConcreteStringVisitorContext self() {
        return this;
    }
}
