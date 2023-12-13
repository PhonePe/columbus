package com.phonepe.platform.columbus.core.utils;

import com.phonepe.platform.columbus.command.Command;
import com.phonepe.platform.columbus.command.CommandHelpers;
import com.phonepe.platform.columbus.command.Executor;
import com.phonepe.platform.columbus.command.factory.HandlerRegistryFactory;
import com.phonepe.platform.columbus.command.impl.CommandHandler;
import com.phonepe.platform.columbus.core.settings.RegistrySettings;
import com.phonepe.platform.columbus.reflect.Generics;
import com.phonepe.platform.columbus.registry.Registries;
import com.phonepe.platform.columbus.registry.Registry;
import com.phonepe.platform.columbus.registry.factory.RegistryOptions;
import com.phonepe.platform.columbus.visitor.VisitorHelpers;
import com.phonepe.platform.columbus.visitor.factory.VisitorRegistryFactory;
import com.phonepe.platform.columbus.visitor.impl.EntityVisitor;
import com.phonepe.platform.columbus.visitor.impl.VisitorContext;
import lombok.experimental.UtilityClass;

import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.phonepe.platform.columbus.common.Predicates.applyIfNotNull;

/**
 * A collection of utilities to create {@link Registry Registries} for {@link EntityVisitor Visitor}s and
 * {@link CommandHandler}s.
 */
@UtilityClass
public class RegistryCreators {

    public <K, R, B, P, C extends VisitorContext<R, B, P, C>, V extends EntityVisitor<R, ? extends B, B, P, C>>
    Registry<K, V> createVisitorRegistry(
            final VisitorRegistryFactory factory,
            final Class<V> visitorType,
            final Class<C> contextType,
            final Class<K> baseType,
            final Function<K, String> nameExtractor,
            @Nullable final RegistrySettings overrides
    ) {
        return Registries.chaining(
                        createVisitorRegistryByName(factory, visitorType, contextType, nameExtractor, overrides),
                        createVisitorRegistryByType(factory, visitorType, contextType, baseType, overrides))
                .build();
    }

    @SuppressWarnings("java:S1452")
    public <E extends Executor, H extends CommandHandler<?, ? extends Command<?>, E>>
    Registry<Command<?>, CommandHandler<?, ? extends Command<?>, ? super E>> createHandlerRegistry(
            final HandlerRegistryFactory factory,
            final Class<H> handlerType,
            final Class<E> executorType,
            @Nullable final RegistrySettings overrides
    ) {
        return Registries.chaining(
                        createHandlerRegistryByName(factory, handlerType, executorType, overrides),
                        createHandlerRegistryByType(factory, handlerType, executorType, overrides))
                .build();
    }

    public <K, R, B, P, C extends VisitorContext<R, B, P, C>, V extends EntityVisitor<R, ? extends B, B, P, C>>
    Registry<K, V> createVisitorRegistryByType(
            final VisitorRegistryFactory factory,
            final Class<V> visitorType,
            final Class<C> contextType,
            final Class<K> baseType,
            @Nullable final RegistrySettings overrides
    ) {
        final var options = VisitorHelpers.optionsForType(visitorType, contextType);
        populateTypeOverrides(options, overrides);
        return Registries.usingClassHierarchy(factory.create(options.build()), baseType);
    }

    public <K, R, B, P, C extends VisitorContext<R, B, P, C>, V extends EntityVisitor<R, ? extends B, B, P, C>>
    Registry<K, V> createVisitorRegistryByName(
            final VisitorRegistryFactory factory,
            final Class<V> visitorType,
            final Class<C> contextType,
            final Function<K, String> nameExtractor,
            @Nullable final RegistrySettings overrides
    ) {
        final var options = VisitorHelpers.optionsForName(visitorType, contextType);
        populateNameOverrides(options, overrides);
        return Registries.transformingKey(nameExtractor, factory.create(options.build()));
    }

    @SuppressWarnings("java:S1452")
    public <E extends Executor, H extends CommandHandler<?, ? extends Command<?>, E>>
    Registry<Command<?>, CommandHandler<?, ? extends Command<?>, ? super E>> createHandlerRegistryByType(
            final HandlerRegistryFactory factory,
            final Class<H> handlerType,
            final Class<E> executorType,
            @Nullable final RegistrySettings overrides
    ) {
        final var options = CommandHelpers.optionsForType(Generics.typed(handlerType), executorType);
        populateTypeOverrides(options, overrides);
        return Registries.usingClassHierarchy(factory.create(options.build()), Generics.typed(Command.class));
    }

    @SuppressWarnings("java:S1452")
    public <E extends Executor, H extends CommandHandler<?, ? extends Command<?>, E>>
    Registry<Command<?>, CommandHandler<?, ? extends Command<?>, ? super E>> createHandlerRegistryByName(
            final HandlerRegistryFactory factory,
            final Class<H> handlerType,
            final Class<E> executorType,
            @Nullable final RegistrySettings overrides
    ) {
        final var options = CommandHelpers.optionsForName(Generics.typed(handlerType), executorType);
        populateNameOverrides(options, overrides);
        return Registries.transformingKey(Command::name, factory.create(options.build()));
    }

    public <V> void populateTypeOverrides(
            final RegistryOptions.RegistryOptionsBuilder<Class<?>, V, ?, ?> options,
            @Nullable final RegistrySettings settings
    ) {
        populateRegistryOverrides(options, settings, RegistrySettings::getDefaultTypeValidator);
    }

    public <V> void populateNameOverrides(
            final RegistryOptions.RegistryOptionsBuilder<String, V, ?, ?> options,
            @Nullable final RegistrySettings settings
    ) {
        populateRegistryOverrides(options, settings, RegistrySettings::getDefaultNameValidator);
    }

    private <K, V> void populateRegistryOverrides(
            final RegistryOptions.RegistryOptionsBuilder<K, V, ?, ?> options,
            @Nullable final RegistrySettings overrides,
            final Function<RegistrySettings, Predicate<K>> keyValidatorExtractor
    ) {
        applyIfNotNull(overrides, settings -> {
            applyIfNotNull(settings.getDefaultTypeFilter(), typeFilter -> typeFilter::test, options::typeFilter);
            applyIfNotNull(keyValidatorExtractor.apply(settings), options::keyValidator);
        });
    }
}
