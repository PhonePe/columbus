package com.phonepe.columbus.test.extensions;

import com.phonepe.columbus.core.toolkit.Toolkit;
import com.phonepe.columbus.dropwizard.internal.storage.factory.DropwizardStorageClientFactory;
import com.phonepe.columbus.dropwizard.storage.DropwizardStorages;
import com.phonepe.columbus.storage.settings.StorageSettings;
import com.phonepe.columbus.test.annotations.WithDropwizardStorageFactory;
import com.phonepe.columbus.test.value.AnnotatedValueExtension;
import com.phonepe.columbus.test.value.ValueProps;
import io.dropwizard.setup.Environment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Optional;

import static com.phonepe.columbus.test.utils.AnnotatedValueUtils.getRequiredValue;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DropwizardStorageFactoryExtension extends AnnotatedValueExtension<DropwizardStorageClientFactory> {

    @NonNull
    @Builder.Default
    private final StorageSettings settings = StorageSettings.defaults();

    private Object toolkitKey;

    private Object environmentKey;

    @Override
    protected DropwizardStorageClientFactory initialize(final ExtensionContext context) {
        final var store = getValueStore(context);
        final var annotation = getAnnotation(context);
        final var toolkitProps = annotation.map(WithDropwizardStorageFactory::toolkit).orElse(null);
        final var toolkit = getRequiredValue(store, toolkitKey, toolkitProps, Toolkit.class);
        final var envProps = annotation.map(WithDropwizardStorageFactory::environment).orElse(null);
        final var environment = getRequiredValue(store, environmentKey, envProps, Environment.class);
        return DropwizardStorages.storageClientFactory(settings, toolkit, environment);
    }

    @Override
    protected Class<DropwizardStorageClientFactory> getType() {
        return DropwizardStorageClientFactory.class;
    }

    @Override
    protected Optional<ValueProps> getProps(final ExtensionContext context) {
        return getAnnotation(context).map(WithDropwizardStorageFactory::value);
    }

    private static Optional<WithDropwizardStorageFactory> getAnnotation(final ExtensionContext context) {
        return AnnotationSupport.findAnnotation(context.getTestClass(), WithDropwizardStorageFactory.class);
    }
}
