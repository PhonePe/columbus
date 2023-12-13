package com.phonepe.platform.columbus.internal.visitor.example.visitors.string;

import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public final class StringVisitor extends StringVisitorContext<StringVisitor> {

    @Override
    protected @NonNull StringVisitor self() {
        return this;
    }
}
