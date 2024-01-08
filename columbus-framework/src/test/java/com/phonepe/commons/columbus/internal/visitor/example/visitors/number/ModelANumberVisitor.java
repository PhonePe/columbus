package com.phonepe.commons.columbus.internal.visitor.example.visitors.number;

import com.phonepe.commons.columbus.internal.visitor.example.models.ModelA;
import com.phonepe.commons.columbus.internal.visitor.example.visitors.ModelVisitor;
import com.phonepe.commons.columbus.internal.visitor.example.visitors.ModelVisitorContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;

@RegistryKey(name = "A")
public class ModelANumberVisitor<C extends ModelVisitorContext<Number, C>> implements ModelVisitor<ModelA, Number, C> {

    @Override
    public String visit(final ModelA model, final Number props, final C context) {
        return "AN:A:%s:%s:%s".formatted(context.getPrefix(), model.getValue(), props);
    }
}
