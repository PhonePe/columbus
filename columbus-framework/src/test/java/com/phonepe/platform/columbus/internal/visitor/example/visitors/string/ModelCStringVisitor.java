package com.phonepe.platform.columbus.internal.visitor.example.visitors.string;

import com.phonepe.platform.columbus.internal.visitor.example.models.ModelC;
import com.phonepe.platform.columbus.internal.visitor.example.visitors.ModelVisitor;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;

@RegistryKey(name = "C", type = ModelC.class)
public class ModelCStringVisitor implements ModelVisitor<ModelC, String, ConcreteStringVisitorContext> {

    @Override
    public String visit(final ModelC model, final String props, final ConcreteStringVisitorContext context) {
        return "CS:C:%s:%s".formatted(context.getPrefix(), props);
    }
}
