package com.phonepe.columbus.internal.visitor.example.visitors;

import com.phonepe.columbus.internal.visitor.example.models.Model;
import com.phonepe.columbus.visitor.impl.EntityVisitor;

public interface ModelVisitor<T extends Model, P, C extends ModelVisitorContext<P, C>>
        extends EntityVisitor<String, T, Model, P, C> {

    @Override
    String visit(T model, P props, C context);
}
