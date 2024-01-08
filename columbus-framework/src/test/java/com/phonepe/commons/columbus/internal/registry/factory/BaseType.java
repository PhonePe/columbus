package com.phonepe.commons.columbus.internal.registry.factory;

import com.phonepe.commons.columbus.registry.factory.RegistryKey;

interface BaseType {

    interface SubType extends BaseType {}

    @RegistryKey(name = "A")
    @RegistryKey(type = BaseType.class, priority = 20)
    class TypeA implements BaseType {
    }

    @RegistryKey(name = "B", type = BaseType.class, priority = 10)
    class TypeB implements BaseType {
    }

    @RegistryKey(name = "A", type = SubType.class)
    class TypeA2 implements SubType {
    }

    @RegistryKey(name = "B")
    class TypeB2 implements SubType {}
}
