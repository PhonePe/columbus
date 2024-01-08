package com.phonepe.commons.columbus.internal.visitor.example.visitors.number;

import com.phonepe.commons.columbus.internal.visitor.example.models.ModelA;
import com.phonepe.commons.columbus.internal.visitor.example.visitors.ModelVisitor;
import com.phonepe.commons.columbus.internal.visitor.example.visitors.ModelVisitorContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;

@RegistryKey(name = "A", priority = 1)
public class ModelAIntegerVisitor<C extends ModelVisitorContext<Integer, C>>
        implements ModelVisitor<ModelA, Integer, C> {

    @Override
    public String visit(final ModelA model, final Integer props, final C context) {
        return "AI:A:%s:%s:%s".formatted(context.getPrefix(), model.getValue(), props);
    }
}
