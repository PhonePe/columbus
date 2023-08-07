package com.phonepe.platform.columbus.visitor;

import com.phonepe.platform.columbus.internal.visitor.VisitorRegistryResolver;
import com.phonepe.platform.columbus.internal.visitor.factory.DelegatingVisitorRegistryFactory;
import com.phonepe.platform.columbus.reflect.Generics;
import com.phonepe.platform.columbus.registry.Registry;
import com.phonepe.platform.columbus.registry.factory.RegistryEntry;
import com.phonepe.platform.columbus.registry.factory.RegistryFactory;
import com.phonepe.platform.columbus.visitor.factory.VisitorRegistryOptions;
import com.phonepe.platform.columbus.visitor.impl.EntityVisitor;
import com.phonepe.platform.columbus.visitor.impl.VisitorContext;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Predicate;

/**
 * A collection of helper methods to create different instances required to create a {@link Visitor}.
 */
@UtilityClass
public class VisitorHelpers {

    public <R, B, C extends VisitorContext<R, B, C>> VisitorRegistryResolver<R, B, C> resolver(
            final Registry<? super B, ? extends EntityVisitor<? extends R, ? extends B, B, ? super C>> registry
    ) {
        return VisitorRegistryResolver.<R, B, C>builder()
                .registry(registry)
                .build();
    }

    public DelegatingVisitorRegistryFactory factory(final RegistryFactory registryFactory) {
        return DelegatingVisitorRegistryFactory.of(registryFactory);
    }

    @SuppressWarnings("java:S1452")
    public <R, B, C extends VisitorContext<R, B, C>, V extends EntityVisitor<R, ? extends B, B, C>>
    VisitorRegistryOptions.VisitorRegistryOptionsBuilder<String, R, B, C, V, ?, ?> optionsForName(
            final Class<V> baseVisitorType,
            final Class<C> contextType
    ) {
        return VisitorRegistryOptions.builder(baseVisitorType, contextType, RegistryEntry::name)
                .keyValidator(StringUtils::isNotEmpty);
    }

    @SuppressWarnings("java:S1452")
    public <R, B, C extends VisitorContext<R, B, C>, V extends EntityVisitor<R, ? extends B, B, C>>
    VisitorRegistryOptions.VisitorRegistryOptionsBuilder<Class<?>, R, B, C, V, ?, ?> optionsForType(
            final Class<V> baseVisitorType,
            final Class<C> contextType
    ) {
        return VisitorRegistryOptions.<Class<?>, R, B, C, V>builder(baseVisitorType, contextType, RegistryEntry::type)
                .keyValidator(Predicate.not(Void.class::equals));
    }

    public <R, B, C extends VisitorContext<R, B, C>, V extends EntityVisitor<R, ? extends B, B, C>>
    boolean acceptsContext(final Class<? extends V> visitorType, final Class<C> contextType) {
        final var parameter = Generics.getTypeParameter(visitorType, EntityVisitor.class.getTypeParameters()[3]);
        return Generics.isSuperType(parameter, contextType);
    }
}
