/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phonepe.columbus.visitor;

import com.phonepe.columbus.internal.visitor.VisitorRegistryResolver;
import com.phonepe.columbus.internal.visitor.factory.DelegatingVisitorRegistryFactory;
import com.phonepe.columbus.reflect.Generics;
import com.phonepe.columbus.registry.Registry;
import com.phonepe.columbus.registry.factory.RegistryEntry;
import com.phonepe.columbus.registry.factory.RegistryFactory;
import com.phonepe.columbus.visitor.factory.VisitorRegistryOptions;
import com.phonepe.columbus.visitor.impl.EntityVisitor;
import com.phonepe.columbus.visitor.impl.VisitorContext;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Predicate;

/**
 * A collection of helper methods to create different instances required to create a {@link Visitor}.
 */
@UtilityClass
public class VisitorHelpers {

    public <R, B, P, C extends VisitorContext<R, B, P, C>> VisitorRegistryResolver<R, B, P, C> resolver(
            final Registry<? super B, ? extends EntityVisitor<? extends R, ? extends B, B, ? super P, ? super C>> registry
    ) {
        return VisitorRegistryResolver.<R, B, P, C>builder()
                .registry(registry)
                .build();
    }

    public DelegatingVisitorRegistryFactory factory(final RegistryFactory registryFactory) {
        return DelegatingVisitorRegistryFactory.of(registryFactory);
    }

    @SuppressWarnings("java:S1452")
    public <R, B, P, C extends VisitorContext<R, B, P, C>, V extends EntityVisitor<R, ? extends B, B, P, C>>
    VisitorRegistryOptions.VisitorRegistryOptionsBuilder<String, R, B, P, C, V, ?, ?> optionsForName(
            final Class<V> baseVisitorType,
            final Class<C> contextType
    ) {
        return VisitorRegistryOptions.builder(baseVisitorType, contextType, RegistryEntry::name)
                .keyValidator(StringUtils::isNotEmpty);
    }

    @SuppressWarnings("java:S1452")
    public <R, B, P, C extends VisitorContext<R, B, P, C>, V extends EntityVisitor<R, ? extends B, B, P, C>>
    VisitorRegistryOptions.VisitorRegistryOptionsBuilder<Class<?>, R, B, P, C, V, ?, ?> optionsForType(
            final Class<V> baseVisitorType,
            final Class<C> contextType
    ) {
        return VisitorRegistryOptions.<Class<?>, R, B, P, C, V>builder(
                        baseVisitorType,
                        contextType,
                        RegistryEntry::type)
                .keyValidator(Predicate.not(Void.class::equals));
    }

    public <R, B, P, C extends VisitorContext<R, B, P, C>, V extends EntityVisitor<R, ? extends B, B, P, C>>
    boolean acceptsContext(final Class<? extends V> visitorType, final Class<C> contextType) {
        final var contextParameter = Generics.getTypeParameter(visitorType, EntityVisitor.class.getTypeParameters()[4]);
        if (Generics.isSuperType(contextParameter, contextType)) {
            return true;
        }
        if (!Generics.isSuperType(contextParameter, contextType.getSuperclass())) {
            return false;
        }
        final var propsType = Generics.getTypeParameter(contextType, VisitorContext.class.getTypeParameters()[2]);
        final var propsParameter = Generics.getTypeParameter(visitorType, EntityVisitor.class.getTypeParameters()[3]);
        return Generics.isSuperType(propsParameter, propsType);
    }
}
