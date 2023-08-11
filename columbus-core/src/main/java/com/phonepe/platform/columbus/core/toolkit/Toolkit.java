package com.phonepe.platform.columbus.core.toolkit;

import com.phonepe.platform.columbus.command.factory.HandlerRegistryFactory;
import com.phonepe.platform.columbus.reflect.Reflect;
import com.phonepe.platform.columbus.registry.factory.RegistryFactory;
import com.phonepe.platform.columbus.serde.SerDe;
import com.phonepe.platform.columbus.visitor.factory.VisitorRegistryFactory;

/**
 * A collection of all the building blocks used by Columbus.
 */
public interface Toolkit {

    Reflect reflect();

    SerDe serDe();

    RegistryFactory registryFactory();

    VisitorRegistryFactory visitorFactory();

    HandlerRegistryFactory handlerFactory();
}
