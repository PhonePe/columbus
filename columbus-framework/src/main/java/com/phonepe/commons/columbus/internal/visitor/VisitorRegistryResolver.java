package com.phonepe.commons.columbus.internal.visitor;

import com.phonepe.commons.columbus.registry.Registry;
import com.phonepe.commons.columbus.visitor.impl.EntityVisitor;
import com.phonepe.commons.columbus.visitor.impl.VisitorContext;
import com.phonepe.commons.columbus.visitor.impl.VisitorResolver;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * A visitor resolver that uses a {@link Registry} to get a visitor for the given value.
 */
@Getter
@SuperBuilder
public class VisitorRegistryResolver<R, B, P, C extends VisitorContext<R, B, P, C>>
        implements VisitorResolver<R, B, P, C> {

    @NonNull
    private final Registry<? super B, ? extends EntityVisitor<? extends R, ? extends B, B, ? super P, ? super C>>
            registry;

    @Override
    public @NonNull <T extends B> EntityVisitor<? extends R, T, ? super B, ? super P, ? super C> visitorFor(final T value) {
        return (EntityVisitor<? extends R, T, ? super B, ? super P, ? super C>) registry.get(value)
                .orElseThrow(() -> new IllegalArgumentException("No visitor registered for value: " + value));
    }
}
