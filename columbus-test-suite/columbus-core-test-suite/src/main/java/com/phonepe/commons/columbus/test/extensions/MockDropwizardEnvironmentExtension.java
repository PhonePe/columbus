package com.phonepe.commons.columbus.test.extensions;

import com.phonepe.commons.columbus.test.annotations.WithMockDropwizardEnvironment;
import com.phonepe.commons.columbus.test.value.AnnotatedValueExtension;
import com.phonepe.commons.columbus.test.value.ValueProps;
import io.dropwizard.setup.Environment;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import org.mockito.Mockito;

import java.util.Optional;

public class MockDropwizardEnvironmentExtension extends AnnotatedValueExtension<Environment> {

    @Override
    protected Environment initialize(final ExtensionContext context) {
        return Mockito.mock(Mockito.RETURNS_DEEP_STUBS);
    }

    @Override
    protected Class<Environment> getType() {
        return Environment.class;
    }

    @Override
    protected Optional<ValueProps> getProps(final ExtensionContext context) {
        return AnnotationSupport.findAnnotation(context.getTestClass(), WithMockDropwizardEnvironment.class)
                .map(WithMockDropwizardEnvironment::value);
    }
}
