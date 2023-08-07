package com.phonepe.platform.columbus.internal.visitor.factory;

import com.phonepe.platform.columbus.registry.Registry;
import com.phonepe.platform.columbus.registry.factory.RegistryFactory;
import com.phonepe.platform.columbus.registry.factory.RegistryOptions;
import com.phonepe.platform.columbus.visitor.VisitorHelpers;
import com.phonepe.platform.columbus.visitor.factory.VisitorRegistryFactory;
import com.phonepe.platform.columbus.visitor.factory.VisitorRegistryOptions;
import com.phonepe.platform.columbus.visitor.impl.EntityVisitor;
import com.phonepe.platform.columbus.visitor.impl.VisitorContext;
import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * A factory for creating a registry of {@link EntityVisitor}s for a fixed {@link VisitorContext}, that uses
 * {@link RegistryFactory}.
 */
@AllArgsConstructor(staticName = "of")
public class DelegatingVisitorRegistryFactory implements VisitorRegistryFactory {

    @NonNull
    private final RegistryFactory registryFactory;

    @Override
    public <K, R, B, C extends VisitorContext<R, B, C>, V extends EntityVisitor<R, ? extends B, B, C>>
    Registry<K, V> create(final VisitorRegistryOptions<K, R, B, C, V> options) {
        final var contextType = options.contextType();

        final var registryOptions = RegistryOptions.builder(options.valueType(), options.keyExtractor())
                .typeFilter(options.typeFilter().and(type -> VisitorHelpers.acceptsContext(type, contextType)))
                .keyValidator(options.keyValidator())
                .ignorePriorityConflicts(options.ignorePriorityConflicts())
                .build();

        return registryFactory.create(registryOptions);
    }
}
