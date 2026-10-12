package com.phonepe.columbus.core.toolkit;

import com.phonepe.columbus.command.factory.HandlerRegistryFactory;
import com.phonepe.columbus.reflect.Reflect;
import com.phonepe.columbus.registry.factory.RegistryFactory;
import com.phonepe.columbus.serde.SerDe;
import com.phonepe.columbus.type.TypeResolver;
import com.phonepe.columbus.visitor.factory.VisitorRegistryFactory;

/**
 * A collection of all the building blocks used by Columbus.
 */
public interface Toolkit {

    Reflect reflect();

    SerDe serDe();

    TypeResolver typeResolver();

    RegistryFactory registryFactory();

    VisitorRegistryFactory visitorFactory();

    HandlerRegistryFactory handlerFactory();
}
