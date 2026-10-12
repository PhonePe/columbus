package com.phonepe.columbus.internal.visitor.example.visitors.number;

import com.phonepe.columbus.internal.visitor.example.visitors.ModelVisitorContext;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public abstract class AbstractNumberVisitorContext<P extends Number, C extends AbstractNumberVisitorContext<P, C>>
        extends ModelVisitorContext<P, C> {
}
