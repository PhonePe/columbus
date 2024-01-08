package com.phonepe.commons.columbus.internal.visitor.example.visitors.number;

import com.phonepe.commons.columbus.internal.visitor.example.models.ModelB;
import com.phonepe.commons.columbus.internal.visitor.example.visitors.ModelVisitor;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;

@RegistryKey(name = "B")
public class ModelBNumberVisitor<C extends NumberVisitorContext<C>> implements ModelVisitor<ModelB, Number, C> {

    @Override
    public String visit(final ModelB model, final Number props, final C context) {
        return "BN:B:%s:%s:%s".formatted(context.getPrefix(), model.getValue(), props);
    }
}
