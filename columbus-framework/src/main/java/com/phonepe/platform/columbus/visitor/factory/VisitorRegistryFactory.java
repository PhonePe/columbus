package com.phonepe.platform.columbus.visitor.factory;

import com.phonepe.platform.columbus.registry.Registry;
import com.phonepe.platform.columbus.visitor.impl.EntityVisitor;
import com.phonepe.platform.columbus.visitor.impl.VisitorContext;

/**
 * A factory to create registry of {@link EntityVisitor}s that can be used with a {@link VisitorContext}.
 */
public interface VisitorRegistryFactory {

    <K, R, B, C extends VisitorContext<R, B, C>, V extends EntityVisitor<R, ? extends B, B, C>>
    Registry<K, V> create(VisitorRegistryOptions<K, R, B, C, V> options);
}
