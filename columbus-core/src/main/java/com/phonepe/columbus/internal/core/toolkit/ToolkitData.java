package com.phonepe.columbus.internal.core.toolkit;

import com.phonepe.columbus.command.factory.HandlerRegistryFactory;
import com.phonepe.columbus.core.toolkit.Toolkit;
import com.phonepe.columbus.reflect.Reflect;
import com.phonepe.columbus.registry.factory.RegistryFactory;
import com.phonepe.columbus.serde.SerDe;
import com.phonepe.columbus.type.TypeResolver;
import com.phonepe.columbus.visitor.factory.VisitorRegistryFactory;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * A concrete implementation of {@link Toolkit} ensuring all the fields are initialized with valid values.
 */
@Data
@Accessors(fluent = true)
@SuperBuilder(toBuilder = true)
public class ToolkitData implements Toolkit {

    @NonNull
    private final Reflect reflect;

    @NonNull
    private final SerDe serDe;

    @NonNull
    private final TypeResolver typeResolver;

    @NonNull
    private final RegistryFactory registryFactory;

    @NonNull
    private final VisitorRegistryFactory visitorFactory;

    @NonNull
    private final HandlerRegistryFactory handlerFactory;
}
