package com.phonepe.platform.columbus.visitor;

import com.phonepe.platform.columbus.internal.visitor.VisitorRegistryResolver;
import com.phonepe.platform.columbus.registry.Registry;
import com.phonepe.platform.columbus.visitor.impl.EntityVisitor;
import com.phonepe.platform.columbus.visitor.impl.VisitorContext;
import lombok.experimental.UtilityClass;

/**
 * A collection of helper methods to create different instances required to create a {@link Visitor}.
 */
@UtilityClass
public class VisitorHelpers {

    public <R, B, C extends VisitorContext<R, B, C>> VisitorRegistryResolver<R, B, C> resolver(
            final Registry<? super B, ? extends EntityVisitor<? extends R, ? extends B, B, ? super C>> registry
    ) {
        return VisitorRegistryResolver.<R, B, C>builder()
                .registry(registry)
                .build();
    }
}
