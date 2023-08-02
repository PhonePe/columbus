package com.phonepe.platform.columbus.internal.visitor;

import com.phonepe.platform.columbus.registry.Registry;
import com.phonepe.platform.columbus.visitor.impl.EntityVisitor;
import com.phonepe.platform.columbus.visitor.impl.VisitorContext;
import com.phonepe.platform.columbus.visitor.impl.VisitorResolver;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * A visitor resolver that uses a {@link Registry} to get a visitor for the given value.
 */
@Getter
@SuperBuilder
public class VisitorRegistryResolver<R, B, C extends VisitorContext<R, B, C>> implements VisitorResolver<R, B, C> {

    @NonNull
    private final Registry<? super B, ? extends EntityVisitor<? extends R, ? extends B, B, ? super C>> registry;

    @Override
    public @NonNull <T extends B> EntityVisitor<? extends R, T, ? super B, ? super C> visitorFor(final T value) {
        return (EntityVisitor<? extends R, T, ? super B, ? super C>) registry.get(value)
                .orElseThrow(() -> new IllegalArgumentException("No visitor registered for value: " + value));
    }
}
