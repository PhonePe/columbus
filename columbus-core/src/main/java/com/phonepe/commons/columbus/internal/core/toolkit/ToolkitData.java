package com.phonepe.commons.columbus.internal.core.toolkit;

import com.phonepe.commons.columbus.command.factory.HandlerRegistryFactory;
import com.phonepe.commons.columbus.core.toolkit.Toolkit;
import com.phonepe.commons.columbus.reflect.Reflect;
import com.phonepe.commons.columbus.registry.factory.RegistryFactory;
import com.phonepe.commons.columbus.serde.SerDe;
import com.phonepe.commons.columbus.visitor.factory.VisitorRegistryFactory;
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
    private final RegistryFactory registryFactory;

    @NonNull
    private final VisitorRegistryFactory visitorFactory;

    @NonNull
    private final HandlerRegistryFactory handlerFactory;
}
