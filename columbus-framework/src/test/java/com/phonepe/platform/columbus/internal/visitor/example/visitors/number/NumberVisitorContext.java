package com.phonepe.platform.columbus.internal.visitor.example.visitors.number;

import com.phonepe.platform.columbus.internal.visitor.example.visitors.ModelVisitorContext;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public abstract class NumberVisitorContext<C extends NumberVisitorContext<C>> extends ModelVisitorContext<Number, C> {
}
