package com.phonepe.columbus.test.extensions;

import com.phonepe.columbus.test.annotations.WithUniqueString;
import com.phonepe.columbus.test.value.IndexedStringProviderExtension;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Optional;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UniqueStringProviderExtension extends IndexedStringProviderExtension<String> {

    public static final String DEFAULT_FORMAT = "string-%d";

    private static final Namespace NAMESPACE = Namespace.create(UniqueStringProviderExtension.class);

    @Builder.Default
    private String format = DEFAULT_FORMAT;

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return super.supportsParameter(parameterContext, extensionContext)
                && parameterContext.isAnnotated(WithUniqueString.Inject.class);
    }

    @Override
    protected Class<String> getType() {
        return String.class;
    }

    @Override
    protected String createInstance(final String string) {
        return string;
    }

    @Override
    protected Namespace getNamespace(final ExtensionContext context) {
        return NAMESPACE;
    }

    @Override
    protected String getFormat(final ExtensionContext context) {
        return getAnnotation(context)
                .map(WithUniqueString::format)
                .orElse(format);
    }

    private static Optional<WithUniqueString> getAnnotation(final ExtensionContext context) {
        return AnnotationSupport.findAnnotation(context.getTestClass(), WithUniqueString.class);
    }
}
