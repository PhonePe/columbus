package com.phonepe.commons.columbus.core.toolkit;

import com.phonepe.commons.columbus.command.factory.HandlerRegistryFactory;
import com.phonepe.commons.columbus.reflect.Reflect;
import com.phonepe.commons.columbus.registry.factory.RegistryFactory;
import com.phonepe.commons.columbus.serde.SerDe;
import com.phonepe.commons.columbus.visitor.factory.VisitorRegistryFactory;

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
