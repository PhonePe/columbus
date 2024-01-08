package com.phonepe.commons.columbus.test.extensions;

import com.phonepe.commons.columbus.internal.storage.factory.ColumbusStorageClientFactory;
import com.phonepe.commons.columbus.test.annotations.WithMemoryStorage;
import com.phonepe.commons.columbus.test.storage.memory.MemoryStorage;
import com.phonepe.commons.columbus.test.storage.memory.MemoryStorageConfiguration;
import com.phonepe.commons.columbus.test.value.AnnotatedValueExtension;
import com.phonepe.commons.columbus.test.value.ValueProps;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Optional;

import static com.phonepe.commons.columbus.test.utils.AnnotatedValueUtils.getRequiredValue;

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

    private Object factoryKey;

    @Override
    protected MemoryStorage initialize(final ExtensionContext context) {
        final var annotation = getAnnotation(context);
        final var store = getValueStore(context);
        final var factoryProps = annotation.map(WithMemoryStorage::storageFactory).orElse(null);
        final var factory = getRequiredValue(store, factoryKey, factoryProps, ColumbusStorageClientFactory.class);
        return (MemoryStorage) factory.create(configuration);
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
