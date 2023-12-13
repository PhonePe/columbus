package com.phonepe.platform.columbus.internal.visitor.example.visitors.number;

import com.phonepe.platform.columbus.internal.visitor.example.models.ModelB;
import com.phonepe.platform.columbus.internal.visitor.example.visitors.ModelVisitor;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;

@RegistryKey(name = "B")
public class ModelBAnyNumberVisitor<P extends Number, C extends AbstractNumberVisitorContext<P, C>>
        implements ModelVisitor<ModelB, P, C> {

    @Override
    public String visit(final ModelB model, final P props, final C context) {
        return "BA:B:%s:%s:%s".formatted(context.getPrefix(), model.getValue(), props);
    }
}
