package com.phonepe.platform.columbus.test.extensions;

import com.phonepe.platform.columbus.core.toolkit.Toolkit;
import com.phonepe.platform.columbus.internal.storage.factory.ColumbusStorageClientFactory;
import com.phonepe.platform.columbus.storage.Storages;
import com.phonepe.platform.columbus.storage.settings.StorageSettings;
import com.phonepe.platform.columbus.test.annotations.WithStorageFactory;
import com.phonepe.platform.columbus.test.value.AnnotatedValueExtension;
import com.phonepe.platform.columbus.test.value.ValueProps;
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
public class StorageFactoryExtension extends AnnotatedValueExtension<ColumbusStorageClientFactory> {

    @NonNull
    @Builder.Default
    private final StorageSettings settings = StorageSettings.defaults();

    private Object toolkitKey;

    @Override
    protected ColumbusStorageClientFactory initialize(final ExtensionContext context) {
        final var store = getValueStore(context);
        final var toolkitProps = getAnnotation(context).map(WithStorageFactory::toolkit).orElse(null);
        final var toolkit = getRequiredValue(store, toolkitKey, toolkitProps, Toolkit.class);
        return Storages.storageClientFactory(settings, toolkit);
    }

    @Override
    protected Class<ColumbusStorageClientFactory> getType() {
        return ColumbusStorageClientFactory.class;
    }

    @Override
    protected Optional<ValueProps> getProps(final ExtensionContext context) {
        return getAnnotation(context).map(WithStorageFactory::value);
    }

    private static Optional<WithStorageFactory> getAnnotation(final ExtensionContext context) {
        return AnnotationSupport.findAnnotation(context.getTestClass(), WithStorageFactory.class);
    }
}
