package com.phonepe.platform.columbus.internal.visitor.example.visitors.string;

import com.phonepe.platform.columbus.internal.visitor.example.models.ModelA;
import com.phonepe.platform.columbus.internal.visitor.example.visitors.ModelVisitor;
import com.phonepe.platform.columbus.internal.visitor.example.visitors.ModelVisitorContext;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;

@RegistryKey(type = ModelA.class)
public class ModelAStringVisitor<C extends ModelVisitorContext<String, C>> implements ModelVisitor<ModelA, String, C> {

    @Override
    public String visit(final ModelA model, final String props, final C context) {
        return "AS:A:%s:%s:%s".formatted(context.getPrefix(), model.getValue(), props);
    }
}
