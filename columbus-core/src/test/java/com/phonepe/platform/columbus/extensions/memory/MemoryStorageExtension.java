package com.phonepe.platform.columbus.extensions.memory;

import com.phonepe.platform.columbus.command.CommandHelpers;
import com.phonepe.platform.columbus.core.toolkit.Toolkit;
import com.phonepe.platform.columbus.core.utils.RegistryCreators;
import com.phonepe.platform.columbus.reflect.Generics;
import com.phonepe.platform.columbus.storage.StorageCommandHandler;
import com.phonepe.platform.columbus.storage.memory.MemoryStorage;
import com.phonepe.platform.columbus.storage.memory.MemoryStorageConfiguration;
import com.phonepe.platform.columbus.test.extensions.AnnotatedValueExtension;
import com.phonepe.platform.columbus.test.extensions.ValueProps;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Optional;

import static com.phonepe.platform.columbus.test.utils.AnnotatedValueUtils.getRequiredValue;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemoryStorageExtension extends AnnotatedValueExtension<MemoryStorage> {

    @NonNull
    @Builder.Default
    private final MemoryStorageConfiguration configuration = MemoryStorageConfiguration.builder()
            .enabled(true)
            .name("memory-test")
            .build();

    private Object toolkitKey;

    @Override
    protected MemoryStorage initialize(final ExtensionContext context) {
        final var annotation = getAnnotation(context);
        final var store = getValueStore(context);
        final var toolkitProps = annotation.map(WithMemoryStorage::toolkit).orElse(null);
        final var toolkit = getRequiredValue(store, toolkitKey, toolkitProps, Toolkit.class);
        return MemoryStorage.builder()
                .resolver(CommandHelpers.resolver(RegistryCreators.createHandlerRegistry(
                        toolkit.handlerFactory(),
                        Generics.typed(StorageCommandHandler.class),
                        MemoryStorage.class,
                        null)))
                .configuration(configuration)
                .build();
    }

    @Override
    protected Class<MemoryStorage> getType() {
        return MemoryStorage.class;
    }

    @Override
    protected Optional<ValueProps> getProps(final ExtensionContext context) {
        return getAnnotation(context)
                .map(WithMemoryStorage::value);
    }

    private static Optional<WithMemoryStorage> getAnnotation(final ExtensionContext context) {
        return AnnotationSupport.findAnnotation(context.getTestClass(), WithMemoryStorage.class);
    }
}
