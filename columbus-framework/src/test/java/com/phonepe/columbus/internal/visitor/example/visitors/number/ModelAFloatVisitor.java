package com.phonepe.columbus.internal.visitor.example.visitors.number;

import com.phonepe.columbus.internal.visitor.example.models.ModelA;
import com.phonepe.columbus.internal.visitor.example.visitors.ModelVisitor;
import com.phonepe.columbus.internal.visitor.example.visitors.ModelVisitorContext;
import com.phonepe.columbus.registry.factory.RegistryKey;

@RegistryKey(name = "A", priority = 1)
public class ModelAFloatVisitor<C extends ModelVisitorContext<Float, C>> implements ModelVisitor<ModelA, Float, C> {

    @Override
    public String visit(final ModelA model, final Float props, final C context) {
        return "AF:A:%s:%s:%s".formatted(context.getPrefix(), model.getValue(), props);
    }
}
