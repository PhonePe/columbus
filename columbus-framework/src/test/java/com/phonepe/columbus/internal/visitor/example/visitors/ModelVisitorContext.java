package com.phonepe.columbus.internal.visitor.example.visitors;

import com.phonepe.columbus.internal.visitor.example.models.Model;
import com.phonepe.columbus.visitor.impl.VisitorContext;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
public abstract class ModelVisitorContext<P, C extends ModelVisitorContext<P, C>>
        extends VisitorContext<String, Model, P, C> {

    @NonNull
    private final String prefix;
}
