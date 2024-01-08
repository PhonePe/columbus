package com.phonepe.commons.columbus.internal.visitor.example.visitors.number;

import com.phonepe.commons.columbus.internal.visitor.example.models.ModelC;
import com.phonepe.commons.columbus.internal.visitor.example.visitors.ModelVisitor;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;

@RegistryKey(name = "C")
public class ModelCNumberVisitor<C extends AbstractNumberVisitorContext<Number, C>>
        implements ModelVisitor<ModelC, Number, C> {

    @Override
    public String visit(final ModelC model, final Number props, final C context) {
        return "CN:C:%s:%s".formatted(context.getPrefix(), props);
    }
}
