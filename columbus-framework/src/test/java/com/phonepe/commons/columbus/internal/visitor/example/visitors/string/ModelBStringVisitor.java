package com.phonepe.commons.columbus.internal.visitor.example.visitors.string;

import com.phonepe.commons.columbus.internal.visitor.example.models.ModelB;
import com.phonepe.commons.columbus.internal.visitor.example.visitors.ModelVisitor;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;

@RegistryKey(name = "B")
public class ModelBStringVisitor<C extends StringVisitorContext<C>> implements ModelVisitor<ModelB, String, C> {

    @Override
    public String visit(final ModelB model, final String props, final C context) {
        return "BS:B:%s:%s:%s".formatted(context.getPrefix(), model.getValue(), props);
    }
}
