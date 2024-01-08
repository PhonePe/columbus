package com.phonepe.commons.columbus.internal.visitor.example.visitors.string;

import com.phonepe.commons.columbus.internal.visitor.example.visitors.ModelVisitorContext;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public abstract class StringVisitorContext<C extends StringVisitorContext<C>> extends ModelVisitorContext<String, C> {
}
