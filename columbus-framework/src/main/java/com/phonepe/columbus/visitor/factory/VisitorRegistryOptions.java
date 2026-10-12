package com.phonepe.columbus.visitor.factory;

import com.phonepe.columbus.registry.factory.RegistryEntry;
import com.phonepe.columbus.registry.factory.RegistryOptions;
import com.phonepe.columbus.visitor.impl.EntityVisitor;
import com.phonepe.columbus.visitor.impl.VisitorContext;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.function.Function;

/**
 * A model with different parameters to help create a registry of {@link EntityVisitor}s which can be used by a
 * {@link VisitorContext}. The parameters can be tweaked to define which visitors are to be registered, what data
 * resolves to a visitor, and how the visitors should be created.
 *
 * @param <K> the key type of the registry.
 * @param <R> the result type of the visitor.
 * @param <B> the base type the visitor can visit.
 * @param <P> the type of props the visitor supports.
 * @param <C> the type of the context the visitors can work with.
 * @param <V> the type of the entity visitor.
 */
@Data
@Accessors(fluent = true)
@SuperBuilder(toBuilder = true)
public class VisitorRegistryOptions<K, R, B, P, C extends VisitorContext<R, B, P, C>,
        V extends EntityVisitor<R, ? extends B, B, P, C>>
        extends RegistryOptions<K, V> {

    @NonNull
    private final Class<C> contextType;

    public static <K, R, B, P, C extends VisitorContext<R, B, P, C>, V extends EntityVisitor<R, ? extends B, B, P, C>>
    VisitorRegistryOptionsBuilder<K, R, B, P, C, V, ?, ?> builder(
            final Class<V> visitorType,
            final Class<C> contextType,
            final Function<RegistryEntry<? extends V>, K> keyExtractor
    ) {
        return new VisitorRegistryOptionsBuilderImpl<K, R, B, P, C, V>()
                .valueType(visitorType)
                .contextType(contextType)
                .keyExtractor(keyExtractor);
    }
}
