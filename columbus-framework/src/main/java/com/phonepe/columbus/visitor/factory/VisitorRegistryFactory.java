package com.phonepe.columbus.visitor.factory;

import com.phonepe.columbus.registry.Registry;
import com.phonepe.columbus.visitor.impl.EntityVisitor;
import com.phonepe.columbus.visitor.impl.VisitorContext;

/**
 * A factory to create registry of {@link EntityVisitor}s that can be used with a {@link VisitorContext}.
 */
public interface VisitorRegistryFactory {

    <K, R, B, P, C extends VisitorContext<R, B, P, C>, V extends EntityVisitor<R, ? extends B, B, P, C>>
    Registry<K, V> create(VisitorRegistryOptions<K, R, B, P, C, V> options);
}
